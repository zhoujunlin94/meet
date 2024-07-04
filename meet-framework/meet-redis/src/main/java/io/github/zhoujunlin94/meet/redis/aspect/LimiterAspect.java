package io.github.zhoujunlin94.meet.redis.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.common.util.ServletUtils;
import io.github.zhoujunlin94.meet.redis.annotation.Limiter;
import io.github.zhoujunlin94.meet.redis.constant.LimiterType;
import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import io.github.zhoujunlin94.meet.redis.helper.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * 接口限流aop
 *
 * @author zhoujl
 */
@Slf4j
@Lazy
@Aspect
@Component
public class LimiterAspect {

    @Pointcut("@annotation(io.github.zhoujunlin94.meet.redis.annotation.Limiter)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limiter limiterAnnotation = method.getAnnotation(Limiter.class);
        LimiterType limiterType = limiterAnnotation.limiterType();
        String name = limiterAnnotation.name();
        String key;
        String ip = ServletUtils.getClientIP();
        int limiterPeriod = limiterAnnotation.period();
        int limiterCount = limiterAnnotation.count();
        switch (limiterType) {
            case IP:
                key = ip;
                break;
            case NORMAL:
                key = limiterAnnotation.key();
                break;
            default:
                key = method.getName().toUpperCase();
                break;
        }
        String redisKey = StrUtil.join(":", limiterAnnotation.prefix(), key, ip);
        Long count = RedisHelper.execute(RedisConstant.LuaScripts.LIMITER_LUA, Collections.singletonList(redisKey), Arrays.asList(Convert.toStr(limiterCount), Convert.toStr(limiterPeriod)), Long.class);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, redisKey, name);
        if (Objects.nonNull(count) && count <= limiterCount) {
            return point.proceed();
        } else {
            throw MeetException.meet(CommonErrorCode.S_OVER_CALLED);
        }

    }

}
