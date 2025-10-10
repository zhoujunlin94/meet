package io.github.zhoujunlin94.mqtt;

import io.github.zhoujunlin94.mqtt.beanfactorypostprocessor.MqttClientBeanFactoryPostProcessor;
import io.github.zhoujunlin94.mqtt.properties.MqttClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:22
 */
@AutoConfiguration
@EnableConfigurationProperties(MqttClientProperties.class)
public class MqttAutoConfiguration {

    @Bean
    MqttClientBeanFactoryPostProcessor mqttClientBeanFactoryPostProcessor() {
        return new MqttClientBeanFactoryPostProcessor();
    }

}
