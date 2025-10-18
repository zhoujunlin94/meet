package io.github.zhoujunlin94.mqtt.properties;

import lombok.Data;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:26
 */
@Data
public class ClientOptions {

    private boolean cleanSession = false;

    private Integer keepAliveInterval = 60;

    private Integer connectionTimeout = 60;

    private boolean automaticReconnect = true;

}
