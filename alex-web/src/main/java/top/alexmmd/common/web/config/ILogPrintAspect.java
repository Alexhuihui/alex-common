package top.alexmmd.common.web.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


@Aspect
public class ILogPrintAspect {

    private ILogConfiguration config;

    public ILogPrintAspect(ILogConfiguration config) {
        this.config = config;
    }

    @Around(value = "@annotation(io.swagger.v3.oas.annotations.Operation)")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == servletRequestAttributes) {
            return joinPoint.proceed();
        }
        String[] ignoreMethods = config.getIgnoreMethods();
        List<String> ignoreMethodList = Lists.newArrayList();
        if (ignoreMethods != null) {
            ignoreMethodList = Arrays.asList(ignoreMethods);
        }
        String requestURI = servletRequestAttributes.getRequest().getRequestURI();
        if (CollUtil.isNotEmpty(ignoreMethodList) && ignoreMethodList.contains(requestURI)) {
            return joinPoint.proceed();
        }
        long startTime = SystemClock.now();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        Operation apiOperation = getApiOperation(joinPoint);
        String beginTime = DateUtil.now();
        Object result = null;
        String errorMessage = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            throw e;
        } finally {
            try {
                ILogPrintDTO logPrint = new ILogPrintDTO();
                logPrint.setBeginTime(beginTime);
                logPrint.setInputParams(buildInput(joinPoint));
                logPrint.setOutputParams(StrUtil.isNotBlank(errorMessage) ? errorMessage : result);
                logPrint.setOperationName(apiOperation.summary());
                String methodType = "";
                methodType = servletRequestAttributes.getRequest().getMethod();

                log.info("[{}] {}, executeTime: {}ms, info: {}", methodType, requestURI
                        , SystemClock.now() - startTime, JSONUtil.toJsonStr(logPrint));
            } catch (Exception ignored) {
            }
        }
        return result;
    }


    private Object[] buildInput(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                continue;
            }
            if (args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if (args[i] instanceof MultipartFile) {
                printArgs[i] = "file";
            } else {
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }

    public static Operation getApiOperation(ProceedingJoinPoint proceedingJoinPoint)
            throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method declaredMethod = proceedingJoinPoint.getTarget().getClass()
                .getDeclaredMethod(methodSignature.getName(),
                        methodSignature.getMethod().getParameterTypes());
        return declaredMethod.getAnnotation(Operation.class);
    }


}
