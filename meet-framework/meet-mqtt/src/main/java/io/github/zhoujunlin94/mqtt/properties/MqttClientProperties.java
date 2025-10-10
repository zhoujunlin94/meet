package io.github.zhoujunlin94.mqtt.properties;

import io.github.zhoujunlin94.mqtt.constant.MqttConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:20
 */
@Data
@ConfigurationProperties(prefix = MqttConstant.MQTT_PROPERTIES_PREFIX)
public class MqttClientProperties {

    private Map<String, Client> client;

}
