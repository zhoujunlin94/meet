package io.github.zhoujunlin94.meet.redis.annotation;

import io.github.zhoujunlin94.meet.redis.registrar.RedisQueueRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis 队列
 *
 * @author zhoujunlin
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisQueueRegistrar.class)
public @interface EnableRedisQueue {


}
