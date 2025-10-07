package io.github.zhoujunlin94.meet.kafka.properties;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025/10/7 15:21
 */
@Data
public class MeetKafkaConsumerProperties {

    /**
     * 主配置
     */
    private String name;
    private List<String> bootstrapServers;
    private Boolean enableAutoCommit = false;
    private String autoOffsetReset = "latest";
    private Class<?> keyDeserializer;
    private Class<?> valueDeserializer;
    private Integer sessionTimeoutMs = 10000;
    private Integer maxPollRecords = 500;
    private Integer fetchMaxWaitMs = 500;

    /**
     * 多实例配置，每个实例可以配置不同的bootstrapServers、keyDeserializer、valueDeserializer等
     * 非必填，默认与主配置相同  覆盖主配置
     */
    private List<MeetKafkaConsumerProperties> items;

    public Map<String, Object> buildProperties() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.enableAutoCommit);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
        consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, this.sessionTimeoutMs);
        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
        consumerProperties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, this.fetchMaxWaitMs);
        return consumerProperties;
    }

    public Deserializer getKeyDeserializerInstance() {
        try {
            if (Objects.nonNull(this.keyDeserializer)) {
                return (Deserializer) this.keyDeserializer.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return new StringDeserializer();
    }

    public Deserializer getValueDeserializerInstance() {
        try {
            if (Objects.nonNull(this.valueDeserializer)) {
                return (Deserializer) this.valueDeserializer.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return new StringDeserializer();
    }

}
