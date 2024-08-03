package io.github.zhoujunlin94.mqtt.properties;

import lombok.Data;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:20
 */
@Data
public class MqttClientProperties {

    private Map<String, Client> client;

}
