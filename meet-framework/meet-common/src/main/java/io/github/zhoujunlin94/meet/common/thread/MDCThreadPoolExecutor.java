package io.github.zhoujunlin94.meet.common.thread;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zhoujunlin
 * @date 2024/10/22 21:15
 * 线程池包装器: 线程池中的每一个线程都能使用主线程的MDC中的上下文
 */
public class MDCThreadPoolExecutor extends ThreadPoolExecutor {

    public MDCThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 包装 Runnable 以传递 MDC 上下文
     */
    @Override
    public void execute(Runnable command) {
        // 获取主线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        super.execute(() -> {
            if (contextMap != null) {
                // 设置子线程的 MDC 上下文
                MDC.setContextMap(contextMap);
            }
            try {
                // 执行实际任务
                command.run();
            } finally {
                // 清除子线程的 MDC 上下文，防止泄露
                MDC.clear();
            }
        });
    }

    /**
     * 包装 Callable 以传递 MDC 上下文
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return super.submit(() -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            try {
                return task.call();
            } finally {
                MDC.clear();
            }
        });
    }

    @Override
    public Future<?> submit(Runnable task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return super.submit(() -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            try {
                task.run();
            } finally {
                MDC.clear();
            }
        });
    }

}