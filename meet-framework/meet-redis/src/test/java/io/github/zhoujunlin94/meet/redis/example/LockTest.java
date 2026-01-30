package io.github.zhoujunlin94.meet.redis.example;

import cn.hutool.core.date.DateUtil;
import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2023年12月12日 16:57
 * @desc
 */
@Slf4j
@SpringBootTest(classes = RedisApplication.class)
public class LockTest {

    private static final String LOCK_KEY = "anylock";

    @Test
    public void testLock() throws InterruptedException {
        // <2.1> 启动一个线程 A ，去占有锁
        new Thread(() -> {
            // 加锁以后 10 秒钟自动解锁
            // 无需调用 unlock 方法手动解锁
            RedissonHelper.tryLock(LOCK_KEY, 10, 10, TimeUnit.SECONDS);
        }).start();
        // <2.2> 简单 sleep 1 秒，保证线程 A 成功持有锁
        Thread.sleep(1000L);

        // <3> 尝试加锁，最多等待 100 秒，上锁以后 10 秒自动解锁
        log.info("准备开始获得锁时间：{}", DateUtil.now());
        boolean res = RedissonHelper.tryLock(LOCK_KEY, 100, 10, TimeUnit.SECONDS);
        if (res) {
            log.info("实际获得锁时间：{}", DateUtil.now());
        } else {
            log.info("加锁失败：{}", DateUtil.now());
        }
    }

    @Test
    public void testRateLimiter() throws InterruptedException {
        String limiterKey = "myRateLimiter";

        // 初始化：最大流速 = 每 1 秒钟产生 2 个令牌
        RedissonHelper.trySetRate(limiterKey, RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);

        for (int i = 0; i < 5; i++) {
            log.info("{}：获得锁结果({})", DateUtil.now(),
                    RedissonHelper.tryAcquire(limiterKey));
            Thread.sleep(250L);
        }

    }

}
