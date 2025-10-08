package io.github.zhoujunlin94.meet.kafka.properties;

import cn.hutool.core.util.ObjectUtil;
import io.github.zhoujunlin94.meet.kafka.interceptor.RequestIdProducerInterceptor;
import lombok.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:23
 * @desc
 */
@Data
public class MeetKafkaProducerProperties {

    /**
     * 主配置
     */
    private String name;
    private List<String> bootstrapServers;
    private Integer batchSize = 16384;
    private Long bufferMemory = 1048576L;
    private Long lingerMs = 10L;
    private Class<? extends Serializer> keySerializer;
    private Class<? extends Serializer> valueSerializer;
    private String acks = "all";
    private Integer retries = 0;
    private List<Class<? extends ProducerInterceptor>> interceptors;

    /**
     * 多实例配置，每个实例可以配置不同的bootstrap-servers、key-serializer、value-serializer等
     * 非必填，默认与主配置相同  覆盖主配置
     */
    private List<MeetKafkaProducerProperties> items;


    public Map<String, Object> buildProperties() {
        Map<String, Object> producerProperties = new HashMap<>();
        //连接地址
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        //重试，0为不启用重试机制
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, this.retries);
        //控制批处理大小，单位为字节
        producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
        //批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, this.lingerMs);
        //生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        producerProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.bufferMemory);
        producerProperties.put(ProducerConfig.ACKS_CONFIG, this.acks);
        List<Class<? extends ProducerInterceptor>> interceptors = ObjectUtil.defaultIfNull(this.interceptors, new ArrayList<>());
        interceptors.add(RequestIdProducerInterceptor.class);
        producerProperties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        return producerProperties;
    }

    public Serializer getKeySerializerInstance() {
        try {
            if (Objects.nonNull(this.keySerializer)) {
                return (Serializer) this.keySerializer.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return new StringSerializer();
    }

    public Serializer getValueSerializerInstance() {
        try {
            if (Objects.nonNull(this.valueSerializer)) {
                return (Serializer) this.valueSerializer.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return new StringSerializer();
    }

}
