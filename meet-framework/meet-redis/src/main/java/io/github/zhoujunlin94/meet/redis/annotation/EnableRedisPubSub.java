package io.github.zhoujunlin94.meet.redis.annotation;

import io.github.zhoujunlin94.meet.redis.registrar.RedisPubSubRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis 发布订阅
 *
 * @author zhoujunlin
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisPubSubRegistrar.class)
public @interface EnableRedisPubSub {
}
