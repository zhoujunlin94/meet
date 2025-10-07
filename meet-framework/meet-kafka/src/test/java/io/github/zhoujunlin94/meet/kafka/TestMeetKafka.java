package io.github.zhoujunlin94.meet.kafka;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:41
 * @desc
 */
@Slf4j
@SpringBootTest(classes = ExampleApp.class)
public class TestMeetKafka {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private KafkaTemplate<String, String> meetKafkaTemplate;
    @Resource
    private KafkaTemplate<String, String> item1KafkaTemplate;

    @Resource
    private Map<String, KafkaTemplate<String, Object>> kafkaTemplates;

    @Test
    public void print() {
        kafkaTemplates.forEach((name, template) -> {
            log.warn("name: {}, template: {}", name, template);
        });
    }

    @Test
    @SneakyThrows
    public void testSend() {
        SendResult<String, String> ret1 = kafkaTemplate.send("abc", "test1").get();
        log.warn("ret1: {}", ret1);

        SendResult<String, String> ret2 = meetKafkaTemplate.send("abc", "test2").get();
        log.warn("ret2: {}", ret2);

        SendResult<String, String> ret3 = item1KafkaTemplate.send("abc", "test3").get();
        log.warn("ret3: {}", ret3);

    }


}
