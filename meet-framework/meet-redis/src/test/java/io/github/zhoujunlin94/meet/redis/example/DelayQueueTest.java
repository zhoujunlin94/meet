package io.github.zhoujunlin94.meet.redis.example;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2024-12-14-15:37
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "standalone")
@SpringBootTest(classes = ExampleApp.class)
public class DelayQueueTest {

    @Test
    public void offer() {
        RedissonHelper.offer("test", "provider demo msg1", 0, TimeUnit.SECONDS);
        RedissonHelper.offer("test", "provider demo msg2", 60, TimeUnit.SECONDS);
        RedissonHelper.offer("test", "provider demo msg3", 120, TimeUnit.SECONDS);
    }


    @Test
    @SneakyThrows
    public void poll() {
        ThreadUtil.newExecutor().execute(() -> {
            while (true) {
                String msg = RedissonHelper.getDelayedQueue("test")
                        .poll();
                if (StrUtil.isBlank(msg)) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.warn(msg);
                }
            }
        });

        new CountDownLatch(1).await();
    }

}
