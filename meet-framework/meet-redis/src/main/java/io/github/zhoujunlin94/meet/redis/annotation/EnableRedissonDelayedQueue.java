package io.github.zhoujunlin94.meet.redis.annotation;

import io.github.zhoujunlin94.meet.redis.registrar.RedissonDelayedQueueRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redisson 延迟队列
 *
 * @author zhoujunlin
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonDelayedQueueRegistrar.class)
public @interface EnableRedissonDelayedQueue {

}
