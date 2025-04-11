package io.github.zhoujunlin94.mqtt.test.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author zhoujunlin
 * @date 2024/8/3 14:09
 */
@RestController
@RequestMapping("/mqtt/send")
public class MqttSenderController {

    @Resource
    private MqttClient publicMqttClient;


    @GetMapping("test")
    public void sendTest() throws MqttException {
        JSONObject content = new JSONObject().fluentPut("msg", "every body here?").fluentPut("msgId", IdUtil.fastSimpleUUID());
        // 创建消息并设置 QoS
        MqttMessage message = new MqttMessage(content.toJSONString().getBytes(StandardCharsets.UTF_8));
        message.setQos(0);

        // 发布消息
        publicMqttClient.publish("mqtt/test101", message);
    }


}
