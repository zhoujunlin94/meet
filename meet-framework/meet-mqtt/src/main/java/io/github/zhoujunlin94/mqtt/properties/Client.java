package io.github.zhoujunlin94.mqtt.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:24
 */
@Data
public class Client {

    private String id;

    private String serverUri;

    private ClientOptions options;

    private List<SubscribeTopic> subscribes;

    public void check() {
        if (StrUtil.isBlank(serverUri)) {
            throw new RuntimeException("mqtt server uri must not blank");
        }
    }

    public String getId() {
        return StrUtil.blankToDefault(this.id, MqttClient.generateClientId());
    }
}
