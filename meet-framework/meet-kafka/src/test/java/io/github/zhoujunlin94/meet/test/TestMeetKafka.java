package io.github.zhoujunlin94.meet.test;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:41
 * @desc
 */
@Slf4j
@SpringBootTest(classes = ExampleApp.class)
public class TestMeetKafka {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Resource
    private KafkaTemplate<String, Object> meet;
    @Resource
    private KafkaTemplate<String, Object> producerItem1;

    @Resource
    private Map<String, KafkaTemplate<String, Object>> kafkaTemplates;

    @Test
    public void printProducers() {
        kafkaTemplates.forEach((name, template) -> {
            log.warn("name: {}, template: {}", name, template);
        });
    }

    @Test
    @SneakyThrows
    public void testSend() {
        SendResult<String, Object> ret1 = kafkaTemplate.send("abc", "test1").get();
        log.warn("ret1: {}", ret1);

        SendResult<String, Object> ret2 = meet.send("abc", "test2").get();
        log.warn("ret2: {}", ret2);

        SendResult<String, Object> ret3 = producerItem1.send("abc", "test3").get();
        log.warn("ret3: {}", ret3);

    }


    @Test
    @SneakyThrows
    public void testConsumer() {
        // 阻塞 观察消费是否完成
        new CountDownLatch(1).await();
    }


}
