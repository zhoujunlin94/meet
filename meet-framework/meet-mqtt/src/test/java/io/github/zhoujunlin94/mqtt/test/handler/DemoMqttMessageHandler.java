package io.github.zhoujunlin94.mqtt.test.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.mqtt.handler.MqttMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024/8/3 21:40
 */
@Slf4j
@Component
public class DemoMqttMessageHandler implements MqttMessageHandler {

    @Override
    public void handleMessage(String topic, MqttMessage message) {
        log.warn("DemoMqttMessageHandler#handleMessage, topic:{}, message:{}", topic, JSONObject.toJSONString(message));
        String payload = new String(message.getPayload());
        log.warn("======> \n{}", payload);
    }

    @Override
    public List<String> topics() {
        return CollUtil.newArrayList(
                "mqtt/test100",
                "mqtt/test101"
        );
    }

}
