package io.github.zhoujunlin94.meet.redis.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.redis.annotation.RedissonLocker;
import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhoujunlin
 * 加锁切面
 */
@Slf4j
@Lazy
@Aspect
@Component
public class RedissonLockerAspect {

    @Pointcut("@annotation(io.github.zhoujunlin94.meet.redis.annotation.RedissonLocker)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RedissonLocker redissonLocker = method.getAnnotation(RedissonLocker.class);

        String lockKey = redissonLocker.prefix() + ":" + parseLockKeySuffix(redissonLocker.suffixEL(), method, point.getArgs());
        log.warn("lockKey:{}", lockKey);

        boolean locked = false;
        try {
            long waitTime = redissonLocker.waitTime();
            long leaseTime = redissonLocker.leaseTime();
            if (waitTime <= 0) {
                locked = RedissonHelper.tryLock(lockKey);
            } else if (leaseTime <= 0) {
                locked = RedissonHelper.tryLock(lockKey, waitTime, redissonLocker.timeUnit());
            } else {
                locked = RedissonHelper.tryLock(lockKey, waitTime, leaseTime, redissonLocker.timeUnit());
            }
            if (locked) {
                return point.proceed();
            } else {
                log.warn("获取锁失败lockKey->{}", lockKey);
            }
        } finally {
            if (locked) {
                RedissonHelper.unLock(lockKey);
            }
        }

        return null;
    }


    private String parseLockKeySuffix(String suffixEL, Method method, Object[] args) {
        if (StrUtil.isNotBlank(suffixEL)) {
            // 获取方法参数名
            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);

            if (ArrayUtil.isNotEmpty(parameterNames) && ArrayUtil.isNotEmpty(args)) {
                // 创建表达式上下文
                StandardEvaluationContext context = new StandardEvaluationContext();
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }

                // 创建SpEL表达式解析器
                ExpressionParser parser = new SpelExpressionParser();
                Expression exp = parser.parseExpression(suffixEL);
                // 获取参数值
                return Convert.toStr(exp.getValue(context), StrUtil.EMPTY);
            }
        }
        return StrUtil.EMPTY;
    }

}
