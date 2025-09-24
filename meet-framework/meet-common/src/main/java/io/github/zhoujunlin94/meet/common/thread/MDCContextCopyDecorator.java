package io.github.zhoujunlin94.meet.common.thread;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2025年09月24日 19:54
 * @desc
 */
public class MDCContextCopyDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取主线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    // 设置子线程的 MDC 上下文
                    MDC.setContextMap(contextMap);
                }
                // 执行实际任务
                runnable.run();
            } finally {
                // 清除子线程的 MDC 上下文，防止泄露
                MDC.clear();
            }
        };
    }

}
