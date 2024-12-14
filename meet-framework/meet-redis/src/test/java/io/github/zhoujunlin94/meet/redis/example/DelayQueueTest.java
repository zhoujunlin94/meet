package io.github.zhoujunlin94.meet.redis.example;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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

    @Resource
    private RedissonClient redissonClient;

    @Test
    @SneakyThrows
    public void delay() {
        // 2. 创建目标队列
        RQueue<String> targetQueue = redissonClient.getQueue("expiredQueue");

        // 3. 绑定延迟队列
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(targetQueue);

        // 4. 添加延迟任务
        delayedQueue.offer("Task1", 5, TimeUnit.SECONDS); // 延迟 5 秒
        delayedQueue.offer("Task2", 60, TimeUnit.SECONDS); // 延迟 10 秒
        delayedQueue.offer("Task3", 100, TimeUnit.SECONDS); // 延迟 15 秒
        System.out.println("Tasks added to delayed queue");

        // 5. 消费者线程：只处理已经到期的任务
        new Thread(() -> {
            while (true) {
                // 从目标队列中轮询过期的任务
                String task = targetQueue.poll();
                if (task != null) {
                    System.out.println("Processing expired task: " + task);
                } else {
                    //System.out.println("No expired tasks available yet...");
                }
            }
        }).start();

        // 6. 关闭延迟队列和客户端
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            delayedQueue.destroy(); // 销毁延迟队列
            redissonClient.shutdown(); // 关闭客户端
        }));

        new CountDownLatch(1).await();

    }


    @Test
    public void offer() {
        RedissonHelper.offer("testQueue", "provider demo msg2", 60, TimeUnit.SECONDS);
        RedissonHelper.offer("testQueue", "provider demo msg3", 120, TimeUnit.SECONDS);
    }


    @Test
    @SneakyThrows
    public void poll() {
        ThreadUtil.newExecutor().execute(() -> {
            RDelayedQueue<String> delayedQueue = RedissonHelper.getDelayedQueue("testQueue");
            while (true) {
                String msg = delayedQueue.poll();
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
