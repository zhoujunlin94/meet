package io.github.zhoujunlin94.meet.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujl
 * redisson锁注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLocker {

    /**
     * 加锁key的前缀
     */
    String prefix();

    /**
     * 从springel表达式中获取值与前缀拼接形成lockKey
     */
    String suffixEL() default "";

    long waitTime() default 0L;

    long leaseTime() default 0L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
