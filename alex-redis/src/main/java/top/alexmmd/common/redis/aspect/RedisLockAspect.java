package top.alexmmd.common.redis.aspect;

import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import top.alexmmd.common.redis.api.RedisLock;
import top.alexmmd.common.redis.utils.SpelUtil;

/**
 * @author 汪永晖
 * @since 2021/12/22 15:30
 */
@Aspect
@Slf4j
@AllArgsConstructor
public class RedisLockAspect {

    private RedissonClient redissonClient;

    private static final String REDISSON_LOCK_PREFIX = "gk:common:redisLock:";

    /**
     * 解析出spel表达式对应的值
     */
    public String getSpelValue(ProceedingJoinPoint joinPoint, String spel) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        String spelValue = SpelUtil.parse(target, spel, targetMethod, arguments);
        log.info("解析spel的结果是 -> {}", spelValue);
        return spelValue;
    }

    /**
     * 生成 redisKey
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getRedisKey(ProceedingJoinPoint joinPoint, String lockName, String spel) {
        String spelValue = this.getSpelValue(joinPoint, spel);
        String redisKey = REDISSON_LOCK_PREFIX + lockName + StrUtil.COLON + spelValue;
        log.info("生成的 redisKey 是 -> {}", redisKey);
        return redisKey;
    }

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String spel = redisLock.key();
        String lockName = redisLock.lockName();
        String redisLockKey = getRedisKey(joinPoint, lockName, spel);
        RLock rLock = redissonClient.getLock(redisLockKey);
        Object result;
        try {
            rLock.lock(redisLock.expire(), redisLock.timeUnit());
            //执行方法
            result = joinPoint.proceed();
        } catch (InterruptedException interruptedException) {
            log.error("获取分布式锁失败, ", interruptedException);
            throw new RuntimeException("获取分布式锁失败");
        } finally {
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
        return result;
    }
}
