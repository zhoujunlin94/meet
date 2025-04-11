package io.github.zhoujunlin94.meet.redis.example;

import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBlockingQueue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2025-01-15-14:42
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApp.class)
public class DelayedQueueTest {

    @Test
    public void offer() {
        RedissonHelper.offer("test", "task1", 0, TimeUnit.SECONDS);
        RedissonHelper.offer("test", "task2", 10, TimeUnit.SECONDS);
        RedissonHelper.offer("test", "task3", 30, TimeUnit.SECONDS);
    }

    @Test
    public void consumer() {
        RBlockingQueue<String> queue = RedissonHelper.getBlockingQueue("test");

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
