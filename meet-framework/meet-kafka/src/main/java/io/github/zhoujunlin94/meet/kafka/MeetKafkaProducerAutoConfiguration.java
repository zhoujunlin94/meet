package io.github.zhoujunlin94.meet.kafka;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProducerProperties;
import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
public class MeetKafkaProducerAutoConfiguration {

    private final MeetKafkaProperties meetKafkaProperties;
    private final ConfigurableBeanFactory beanFactory;

    @Bean
    public Map<String, KafkaTemplate<String, Object>> kafkaTemplates() {
        Map<String, KafkaTemplate<String, Object>> templates = new HashMap<>();
        if (Objects.isNull(meetKafkaProperties) || Objects.isNull(meetKafkaProperties.getProducer())) {
            return templates;
        }

        MeetKafkaProducerProperties producerProps = meetKafkaProperties.getProducer();

        ProducerFactory<String, Object> meetProducerFactory = new DefaultKafkaProducerFactory<>(producerProps.buildProperties(), producerProps.getKeySerializer(), producerProps.getValueSerializer());
        KafkaTemplate<String, Object> meetKafkaTemplate = new KafkaTemplate<>(meetProducerFactory);
        String beanName = StrUtil.blankToDefault(producerProps.getName(), "meet") + "KafkaTemplate";
        templates.put(beanName, meetKafkaTemplate);
        beanFactory.registerSingleton(beanName, meetKafkaTemplate);

        if (CollUtil.isNotEmpty(producerProps.getItems())) {
            producerProps.getItems().forEach(item -> {
                if (StrUtil.isNotBlank(item.getName()) && CollUtil.isNotEmpty(item.getBootstrapServers())) {
                    ProducerFactory<String, Object> itemProducerFactory = new DefaultKafkaProducerFactory<>(item.buildProperties(), item.getKeySerializer(), item.getValueSerializer());
                    KafkaTemplate<String, Object> itemKafkaTemplate = new KafkaTemplate<>(itemProducerFactory);
                    String itemBeanName = item.getName() + "KafkaTemplate";
                    templates.put(itemBeanName, itemKafkaTemplate);
                    beanFactory.registerSingleton(itemBeanName, itemKafkaTemplate);
                }
            });
        }


        return templates;
    }


}
