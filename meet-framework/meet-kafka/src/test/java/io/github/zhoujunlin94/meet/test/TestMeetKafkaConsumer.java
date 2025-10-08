package io.github.zhoujunlin94.meet.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zhoujunlin
 * @date 2025/10/7 21:14
 */
@Slf4j
@Component
public class TestMeetKafkaConsumer {

    @KafkaListener(
            topics = "abc",
            containerFactory = "main",
            groupId = "test1",
            concurrency = "3"
    )
    public void test1(ConsumerRecord<String, Object> consumerRecord) {
        log.info("main: {}", consumerRecord.value());
    }

    @KafkaListener(
            topics = "abc",
            containerFactory = "consumerItem1",
            groupId = "test2",
            concurrency = "3"
    )
    public void test2(ConsumerRecord<String, Object> consumerRecord) {
        log.info("consumerItem1: {}", consumerRecord.value());
    }


}
