package org.spring.compose.web.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.spring.compose.common.exception.BizException;
import org.spring.compose.common.out.result.ResultCode;
import org.spring.compose.web.annotation.PreventDuplicateResubmit;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DuplicateSubmitAspect {

    private final RedissonClient redissonClient;
    private static final String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT:";

    @Pointcut("@annotation(preventDuplicateResubmit)")
    public void preventDuplicateSubmitPointCut(PreventDuplicateResubmit preventDuplicateResubmit) {
        log.info("防重复提交切点");
    }

    @Around("preventDuplicateSubmitPointCut(preventDuplicateResubmit)")
    public Object aroundExecute(ProceedingJoinPoint joinPoint, PreventDuplicateResubmit preventDuplicateResubmit) throws Throwable {
        var request = getRequest();
        // todo: 获取用户id
        long uid = 10066993;

        // 拼接key
        String lockKey = new StringBuffer(RESUBMIT_LOCK_PREFIX)
                .append(uid)
                .append(":")
                .append(request.getMethod())
                .append("-")
                .append(request.getRequestURI())
                .toString();
        int expire = preventDuplicateResubmit.expire();
        RLock lock = redissonClient.getLock(lockKey);
        // 尝试获取锁
        boolean lockResult = lock.tryLock(0, expire, TimeUnit.SECONDS);
        if (!lockResult) {
            throw new BizException(ResultCode.REPEAT_SUBMIT_ERROR);
        }
        return joinPoint.proceed();
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
