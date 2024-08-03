package io.github.zhoujunlin94.mqtt.properties;

import lombok.Data;

/**
 * @author zhoujunlin
 * @date 2024/8/3 18:27
 */
@Data
public class SubscribeTopic {

    private String topicFilter;

    private Integer qos = 0;

}
