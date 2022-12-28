package top.alexmmd.common.web.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Path.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.alexmmd.common.base.constant.BaseStatusCode;
import top.alexmmd.common.base.exception.BaseException;
import top.alexmmd.common.base.http.response.BaseResponse;

/**
 * @author wangyonghui
 * @date 2022年12月28日 15:35:00
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public BaseResponse bindExceptionHandler(HttpServletResponse response, BindException ex) {
        return getBaseResponse(response, ex);
    }

    private BaseResponse getBaseResponse(HttpServletResponse response, BindException ex) {
        log.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String field = Objects.requireNonNull(fieldError).getField();
        String msg = fieldError.getDefaultMessage();
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(), "[" + field + "]" + msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(HttpServletResponse response,
            MethodArgumentNotValidException ex) {
        return getBaseResponse(response, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResponse methodArgumentTypeMismatchExceptionHandler(HttpServletResponse response,
            MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String field = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String msg = "需要传入的类型：";
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(),
                "[" + field + "]" + msg + requiredType.getTypeName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse constraintViolationExceptionHandler(HttpServletResponse response,
            ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().stream()
                .findFirst().get();
        List<Node> pathList = StreamSupport.stream(
                        constraintViolation.getPropertyPath().spliterator(), false)
                .collect(Collectors.toList());
        String field = pathList.get(pathList.size() - 1).getName();
        String msg = constraintViolation.getMessage();
        return new BaseResponse(BaseStatusCode.PARAM_INVALID.getCode(), "[" + field + "]" + msg);
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse baseExceptionHandler(HttpServletResponse response, BaseException ex) {
        log.error(ex.getMessage(), ex);
        response.setStatus(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode());
        return new BaseResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse anyExceptionHandler(HttpServletResponse response, Exception ex) {
        List<Throwable> throwableList = ExceptionUtil.getThrowableList(ex);
        if (CollUtil.isNotEmpty(throwableList)) {
            for (Throwable throwable : throwableList) {
                if (SQLException.class.isAssignableFrom(throwable.getClass())) {
                    log.error(ex.getMessage(), ex);
                    response.setStatus(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode());
                    return new BaseResponse(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode(),
                            StrUtil.EMPTY);
                }
            }
        }
        log.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new BaseResponse(BaseStatusCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }

}
