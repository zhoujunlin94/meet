package io.github.zhoujunlin94.meet.kafka.interceptor;

import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2023年06月27日 10:30
 * @desc
 */
@Slf4j
public class RequestIdProducerInterceptor implements ProducerInterceptor<String, Object> {

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record) {
        String requestId = MDC.get(RequestIdUtil.REQUEST_ID);
        requestId = StrUtil.blankToDefault(requestId, RequestIdUtil.getRequestId());
        Headers headers = record.headers();
        headers.add(RequestIdUtil.REQUEST_ID, requestId.getBytes(StandardCharsets.UTF_8));
        log.warn("kafka producer onSend, topic: {}, partition: {},  key: {}, value: {}, headers: {}",
                record.topic(), record.partition(), record.key(), record.value(), headers);
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

}
