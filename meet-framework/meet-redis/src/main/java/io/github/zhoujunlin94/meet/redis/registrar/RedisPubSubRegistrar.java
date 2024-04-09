package io.github.zhoujunlin94.meet.redis.registrar;

import cn.hutool.extra.spring.SpringUtil;
import io.github.zhoujunlin94.meet.redis.consumer.RedisPubSubMsgConsumer;
import io.github.zhoujunlin94.meet.redis.dispatcher.PubSubMsgDispatcher;
import io.github.zhoujunlin94.meet.redis.factorybean.RedisMessageListenerContainerFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author zhoujunlin
 * @date 2023年12月13日 11:37
 * @desc
 */
@Slf4j
public class RedisPubSubRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        GenericBeanDefinition pubSubMsgDispatcherBeanDefinition = new GenericBeanDefinition();
        pubSubMsgDispatcherBeanDefinition.setBeanClass(PubSubMsgDispatcher.class);
        registry.registerBeanDefinition("pubSubMsgDispatcher", pubSubMsgDispatcherBeanDefinition);

        GenericBeanDefinition pubSubMsgConsumerBeanDefinition = new GenericBeanDefinition();
        pubSubMsgConsumerBeanDefinition.setBeanClass(RedisPubSubMsgConsumer.class);
        pubSubMsgConsumerBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        registry.registerBeanDefinition("pubSubMsgConsumer", pubSubMsgConsumerBeanDefinition);

        GenericBeanDefinition redisMessageListenerContainerBeanDefinition = new GenericBeanDefinition();
        redisMessageListenerContainerBeanDefinition.setBeanClass(RedisMessageListenerContainer.class);
        redisMessageListenerContainerBeanDefinition.setInstanceSupplier(() -> new RedisMessageListenerContainerFactoryBean().getObject());
        registry.registerBeanDefinition("redisMessageListenerContainer", redisMessageListenerContainerBeanDefinition);

        GenericBeanDefinition redisMessageListenerAdapterBeanDefinition = new GenericBeanDefinition();
        redisMessageListenerAdapterBeanDefinition.setBeanClass(MessageListenerAdapter.class);
        redisMessageListenerAdapterBeanDefinition.setInstanceSupplier(this::messageListenerAdapter);
        registry.registerBeanDefinition("redisPubSubMsgConsumer", redisMessageListenerAdapterBeanDefinition);

    }


    private MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(SpringUtil.getBean(RedisPubSubMsgConsumer.class), "onMessage");
    }

}
