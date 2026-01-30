package io.github.zhoujunlin94.meet.redis.example;

import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingQueue;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2025-01-15-14:42
 */
@Slf4j
@SpringBootTest(classes = RedisApplication.class)
public class DelayedQueueTest {

    @Test
    public void offer() {
        RedissonHelper.offer("testRBlockingQueue", "task1", 0, TimeUnit.SECONDS);
        RedissonHelper.offer("testRBlockingQueue", "task2", 10, TimeUnit.SECONDS);
        RedissonHelper.offer("testRBlockingQueue", "task3", 30, TimeUnit.SECONDS);
    }

    @Test
    public void consumer() {
        RBlockingQueue<String> queue = RedissonHelper.getBlockingQueue("testRBlockingQueue");

        while (true) {
            try {
                String ret = queue.poll(500, TimeUnit.MILLISECONDS);
                if (ret != null) {
                    log.info("消费：{}", ret);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
