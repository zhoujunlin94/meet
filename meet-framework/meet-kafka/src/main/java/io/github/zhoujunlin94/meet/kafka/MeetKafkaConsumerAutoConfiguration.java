package io.github.zhoujunlin94.meet.kafka;

import io.github.zhoujunlin94.meet.kafka.properties.MeetKafkaProperties;
import io.github.zhoujunlin94.meet.kafka.registrar.MeetKafkaConsumerRegistrar;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:17
 * @desc
 */
@AutoConfiguration
@Import(MeetKafkaConsumerRegistrar.class)
@EnableConfigurationProperties(MeetKafkaProperties.class)
@ConditionalOnClass(ConcurrentKafkaListenerContainerFactory.class)
public class MeetKafkaConsumerAutoConfiguration {
}
