package io.github.zhoujunlin94.meet.kafka;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author zhoujunlin
 * @date 2025年09月26日 14:41
 * @desc
 */
@Slf4j
@SpringBootTest(classes = ExampleApp.class)
public class TestMeetKafka {

    @Resource
    private KafkaTemplate kafkaTemplate;
    @Resource
    private KafkaTemplate meetKafkaTemplate;
    @Resource
    private KafkaTemplate item1KafkaTemplate;

    @Test
    public void test() {
        log.info("test");
    }


}
