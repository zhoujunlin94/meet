package io.github.zhoujunlin94.meet.redis.annotation;


import io.github.zhoujunlin94.meet.redis.constant.LimiterType;

import java.lang.annotation.*;

/**
 * @author zhoujunlin
 * 限流注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    /**
     * 资源名称，用于描述接口功能
     */
    String name() default "";

    /**
     * 资源 key
     */
    String key() default "default";

    /**
     * key prefix
     */
    String prefix() default "meet";

    /**
     * 时间范围，单位秒
     */
    int period() default 1;

    /**
     * 限制访问次数
     */
    int count() default 10;

    /**
     * 限制类型
     */
    LimiterType limiterType() default LimiterType.NORMAL;

}
