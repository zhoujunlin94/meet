package io.github.zhoujunlin94.mqtt.handler;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024/8/3
 */
public interface MqttMessageHandler {

    default void handle(String topic, MqttMessage message) {
        if (topics().contains(topic)) {
            handleMessage(topic, message);
        }
    }

    void handleMessage(String topic, MqttMessage message);

    List<String> topics();

}
