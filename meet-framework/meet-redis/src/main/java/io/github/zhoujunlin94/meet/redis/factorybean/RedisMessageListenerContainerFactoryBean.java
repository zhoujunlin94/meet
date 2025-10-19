package io.github.zhoujunlin94.meet.redis.factorybean;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoujunlin
 * @date 2023年12月13日 13:36
 * @desc
 */
@Slf4j
public class RedisMessageListenerContainerFactoryBean implements FactoryBean<RedisMessageListenerContainer> {

    @Override
    public RedisMessageListenerContainer getObject() {
        Binder binder = Binder.get(SpringUtil.getBean(ConfigurableEnvironment.class));
        String topics = binder.bind("spring.redis.message.topics", String.class).orElse(StrUtil.EMPTY);
        log.warn("read spring.redis.message.topics:{}", topics);
        if (StrUtil.isBlank(topics)) {
            throw new RuntimeException("spring.redis.message.topics value is blank");
        }
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(SpringUtil.getBean(RedisConnectionFactory.class));
        // 可以添加多个 messageListener，配置不同的交换机
        List<PatternTopic> patternTopics = StrUtil.splitTrim(topics, StrUtil.COMMA).stream().map(PatternTopic::new).collect(Collectors.toList());
        redisMessageListenerContainer.addMessageListener(SpringUtil.getBean(MessageListenerAdapter.class), patternTopics);
        return redisMessageListenerContainer;
    }

    @Override
    public Class<?> getObjectType() {
        return RedisMessageListenerContainer.class;
    }

}
