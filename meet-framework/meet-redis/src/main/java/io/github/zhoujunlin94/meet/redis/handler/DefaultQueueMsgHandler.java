package io.github.zhoujunlin94.meet.redis.handler;

import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import io.github.zhoujunlin94.meet.redis.queue.TaskItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhoujl
 * @date 2021/4/19 17:31
 * @desc
 */
@Slf4j
@Component(RedisConstant.BeanName.DEFAULT_QUEUE_HANDLER)
public class DefaultQueueMsgHandler implements BaseQueueMsgHandler {

    @Override
    public void handle(TaskItem taskItem) {
        log.info("DefaultQueueMsgHandler接收到信息{}", taskItem);
    }

}
