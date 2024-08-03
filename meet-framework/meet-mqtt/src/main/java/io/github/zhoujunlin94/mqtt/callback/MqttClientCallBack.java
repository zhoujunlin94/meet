package io.github.zhoujunlin94.mqtt.callback;

import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.mqtt.handler.MqttMessageHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * @author zhoujunlin
 * @date 2024/8/3 19:07
 */
@Slf4j
public class MqttClientCallBack implements MqttCallback {

    private Collection<MqttMessageHandler> mqttMessageHandlers;

    public MqttClientCallBack(ApplicationContext applicationContext) {
        this.mqttMessageHandlers = applicationContext.getBeansOfType(MqttMessageHandler.class).values();
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("connectionLost, {}", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.warn("messageArrived, topic:{}, message:{}", topic, JSONObject.toJSONString(message));
        mqttMessageHandlers.forEach(mqttMessageHandler -> mqttMessageHandler.handle(topic, message));
    }

    @Override
    @SneakyThrows
    public void deliveryComplete(IMqttDeliveryToken token) {
        String payload = new String(token.getMessage().getPayload());
        log.warn("deliveryComplete, topics:{}, isComplete:{}, payload:{}", token.getTopics(), token.isComplete(), payload);
    }

}
