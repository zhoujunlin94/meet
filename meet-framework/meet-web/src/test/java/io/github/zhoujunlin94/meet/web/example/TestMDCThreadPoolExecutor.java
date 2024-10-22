package io.github.zhoujunlin94.meet.web.example;

import cn.hutool.core.util.IdUtil;
import io.github.zhoujunlin94.meet.common.thread.MDCThreadPoolExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.concurrent.*;

/**
 * @author zhoujunlin
 * @date 2024/10/22 21:29
 */
@Slf4j
public class TestMDCThreadPoolExecutor {

    @SneakyThrows
    public static void main(String[] args) {
        MDCThreadPoolExecutor executor = new MDCThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() * 2 + 1, 100, 300, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        // 在主线程中设置 MDC 上下文
        MDC.put("traceId", IdUtil.fastSimpleUUID());
        MDC.put("requestTime", "100");

        // 提交任务
        executor.execute(() -> {
            log.warn("子线程中的MDC: " + MDC.getCopyOfContextMap());
        });

        new CountDownLatch(1).await();
    }

}
