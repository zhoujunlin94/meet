package io.github.zhoujunlin94.meet.web.example.test;

import cn.hutool.core.util.IdUtil;
import io.github.zhoujunlin94.meet.web.example.ExampleApp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhoujunlin
 * @date 2025年09月24日 20:04
 * @desc
 */
@Slf4j
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = ExampleApp.class)
public class TestThreadPool {

    @Resource
    private ThreadPoolTaskExecutor meetThreadPool;

    @Test
    @SneakyThrows
    public void testMDCContextCopyDecorator() {
        MDC.put("traceId", IdUtil.fastSimpleUUID());
        MDC.put("requestTime", "100");

        // 提交任务
        meetThreadPool.execute(() -> {
            log.warn("子线程中的MDC: " + MDC.getCopyOfContextMap());
        });

        new CountDownLatch(1).await();
    }


}
