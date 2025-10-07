package io.github.zhoujunlin94.meet.kafka;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaConsumerProperties;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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
@ConditionalOnClass(ConcurrentKafkaListenerContainerFactory.class)
public class MeetKafkaConsumerAutoConfiguration {

    private final MeetKafkaProperties meetKafkaProperties;
    private final ConfigurableBeanFactory beanFactory;
    private static final String CONSUMER_FACTORY_SUFFIX = "_ConsumerFactory";

    @Bean
    public Map<String, ConsumerFactory<String, Object>> consumerFactories() {
        Map<String, ConsumerFactory<String, Object>> consumerFactoryMap = new HashMap<>();
        if (Objects.isNull(meetKafkaProperties) || Objects.isNull(meetKafkaProperties.getConsumer())) {
            return consumerFactoryMap;
        }

        MeetKafkaConsumerProperties consumerProps = meetKafkaProperties.getConsumer();
        Map<String, Object> mainProperties = consumerProps.buildProperties();

        ConsumerFactory<String, Object> mainConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(mainProperties, consumerProps::getKeyDeserializerInstance, consumerProps::getValueDeserializerInstance);
        String beanName = StrUtil.blankToDefault(consumerProps.getName(), "meet") + CONSUMER_FACTORY_SUFFIX;
        consumerFactoryMap.put(beanName, mainConsumerFactory);

        if (CollUtil.isNotEmpty(consumerProps.getItems())) {
            consumerProps.getItems().forEach(item -> {
                if (StrUtil.isNotBlank(item.getName()) && CollUtil.isNotEmpty(item.getBootstrapServers())) {
                    Map<String, Object> itemProperties = new HashMap<>();
                    itemProperties.putAll(mainProperties);
                    itemProperties.putAll(item.buildProperties());

                    ConsumerFactory<String, Object> itemConsumerFactory = new DefaultKafkaConsumerFactory<String, Object>(itemProperties, item::getKeyDeserializerInstance, item::getValueDeserializerInstance);
                    String itemBeanName = item.getName() + CONSUMER_FACTORY_SUFFIX;
                    consumerFactoryMap.put(itemBeanName, itemConsumerFactory);
                }
            });
        }

        return consumerFactoryMap;
    }

    @Bean
    public Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> kafkaListenerContainerFactories() {
        Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> kafkaListenerContainerFactoryMap = new HashMap<>();
        consumerFactories().forEach((consumerFactoryBeanName, consumerFactory) -> {
            ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
            kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
            String beanName = StrUtil.removeSuffix(consumerFactoryBeanName, CONSUMER_FACTORY_SUFFIX);
            kafkaListenerContainerFactoryMap.put(beanName, kafkaListenerContainerFactory);
            beanFactory.registerSingleton(beanName, kafkaListenerContainerFactory);
        });
        return kafkaListenerContainerFactoryMap;
    }

}
