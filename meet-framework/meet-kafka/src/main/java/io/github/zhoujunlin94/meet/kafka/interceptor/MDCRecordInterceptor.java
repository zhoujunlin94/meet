package io.github.zhoujunlin94.meet.kafka.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025/10/8 20:46
 */
@Slf4j
public class MDCRecordInterceptor<K, V> implements RecordInterceptor<K, V> {

    @Override
    public ConsumerRecord<K, V> intercept(@NonNull ConsumerRecord<K, V> record, @NonNull Consumer<K, V> consumer) {
        String requestId = null;
        if (Objects.nonNull(record.headers()) && Objects.nonNull(record.headers().lastHeader(RequestIdUtil.REQUEST_ID))) {
            requestId = new String(record.headers().lastHeader(RequestIdUtil.REQUEST_ID).value(), StandardCharsets.UTF_8);
        }
        requestId = StrUtil.blankToDefault(requestId, RequestIdUtil.getRequestId());
        MDC.put(RequestIdUtil.REQUEST_ID, requestId);
        // 如果有多个消费组的话 同一个消息会被多个消费组消费 所以这里打印日志会重复 所以这里不建议打印  可以在消费者端打印
        log.warn("kafka consumer onReceive, topic: {}, partition: {}, time: {}, key: {}, value: {}, headers: {}", record.topic(), record.partition(), DateUtil.date(record.timestamp()), record.key(), record.value(), record.headers());
        return record;
    }

    @Override
    public void afterRecord(@NonNull ConsumerRecord<K, V> record, @NonNull Consumer<K, V> consumer) {
        MDC.remove(RequestIdUtil.REQUEST_ID);
    }

}
