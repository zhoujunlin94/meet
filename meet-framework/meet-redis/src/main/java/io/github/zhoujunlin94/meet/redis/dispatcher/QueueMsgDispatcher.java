package io.github.zhoujunlin94.meet.redis.dispatcher;

import cn.hutool.extra.spring.SpringUtil;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.redis.handler.BaseQueueMsgHandler;
import io.github.zhoujunlin94.meet.redis.queue.TaskItem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujl
 * @date 2021/4/19 10:33
 * @desc
 */
@Slf4j
public class QueueMsgDispatcher implements InitializingBean {

    private Map<String, BaseQueueMsgHandler> queueMsgHandlerMap;

    @Override
    public void afterPropertiesSet() {
        queueMsgHandlerMap = SpringUtil.getBeansOfType(BaseQueueMsgHandler.class);
    }

    public void dispatch(TaskItem taskItem) {
        try {
            MDC.put(RequestIdUtil.REQUEST_ID, taskItem.getRequestId() + "_RedisQueue");
            log.warn("redis queue接受到消息:{}", taskItem);
            BaseQueueMsgHandler msgHandler = queueMsgHandlerMap.get(taskItem.getHandlerName());
            if (Objects.isNull(msgHandler)) {
                log.error("未找到消息处理器");
                return;
            }
            msgHandler.handle(taskItem);
        } catch (Exception e) {
            log.error("分发队列消息出错", e);
        } finally {
            MDC.remove(RequestIdUtil.REQUEST_ID);
        }
    }

}
