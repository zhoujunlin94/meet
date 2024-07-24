package io.github.zhoujunlin94.meet.redis.registrar;

import io.github.zhoujunlin94.meet.redis.dispatcher.QueueMsgDispatcher;
import io.github.zhoujunlin94.meet.redis.queue.RedisDelayQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zhoujunlin
 * @date 2023年12月13日 11:37
 * @desc
 */
@Slf4j
public class RedisQueueRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        GenericBeanDefinition queueMsgDispatcherBeanDefinition = new GenericBeanDefinition();
        queueMsgDispatcherBeanDefinition.setBeanClass(QueueMsgDispatcher.class);
        registry.registerBeanDefinition("queueMsgDispatcher", queueMsgDispatcherBeanDefinition);

        GenericBeanDefinition redisDelayQueueBeanDefinition = new GenericBeanDefinition();
        redisDelayQueueBeanDefinition.setBeanClass(RedisDelayQueue.class);
        redisDelayQueueBeanDefinition.setDependsOn("redisHelper");
        redisDelayQueueBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        registry.registerBeanDefinition("redisDelayQueue", redisDelayQueueBeanDefinition);
    }

}
