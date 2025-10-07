package io.github.zhoujunlin94.meet.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:16
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "meet.kafka")
public class MeetKafkaProperties {

    @NestedConfigurationProperty
    private MeetKafkaProducerProperties producer;

    @NestedConfigurationProperty
    private MeetKafkaConsumerProperties consumer;

}
