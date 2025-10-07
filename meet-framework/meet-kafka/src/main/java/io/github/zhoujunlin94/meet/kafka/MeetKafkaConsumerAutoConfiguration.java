package io.github.zhoujunlin94.meet.kafka;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaConsumerProperties;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:17
 * @desc
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(MeetKafkaProperties.class)
public class MeetKafkaConsumerAutoConfiguration {

    private final MeetKafkaProperties meetKafkaProperties;
    private final ConfigurableBeanFactory beanFactory;

    @Bean
    public Map<String, ConsumerFactory<String, Object>> consumerFactories() {
        Map<String, ConsumerFactory<String, Object>> consumerFactoryMap = new HashMap<>();
        if (Objects.isNull(meetKafkaProperties) || Objects.isNull(meetKafkaProperties.getConsumer())) {
            return consumerFactoryMap;
        }

        MeetKafkaConsumerProperties consumerProps = meetKafkaProperties.getConsumer();
        Map<String, Object> mainProperties = consumerProps.buildProperties();

        ConsumerFactory<String, Object> mainConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(mainProperties, consumerProps::getKeyDeserializerInstance, consumerProps::getValueDeserializerInstance);
        String beanName = StrUtil.blankToDefault(consumerProps.getName(), "main") + "ConsumerFactory";
        consumerFactoryMap.put(beanName, mainConsumerFactory);
        beanFactory.registerSingleton(beanName, mainConsumerFactory);

        if (CollUtil.isNotEmpty(consumerProps.getItems())) {
            consumerProps.getItems().forEach(item -> {
                if (StrUtil.isNotBlank(item.getName()) && CollUtil.isNotEmpty(item.getBootstrapServers())) {
                    Map<String, Object> itemProperties = new HashMap<>();
                    itemProperties.putAll(mainProperties);
                    itemProperties.putAll(item.buildProperties());

                    ConsumerFactory<String, Object> itemConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(itemProperties, item::getKeyDeserializerInstance, item::getValueDeserializerInstance);
                    String itemBeanName = item.getName() + "ConsumerFactory";
                    consumerFactoryMap.put(itemBeanName, itemConsumerFactory);
                    beanFactory.registerSingleton(itemBeanName, itemConsumerFactory);
                }
            });
        }

        return consumerFactoryMap;
    }

}
