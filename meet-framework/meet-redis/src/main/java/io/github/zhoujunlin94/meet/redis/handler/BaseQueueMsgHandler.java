package io.github.zhoujunlin94.meet.redis.handler;

import io.github.zhoujunlin94.meet.redis.queue.TaskItem;

/**
 * @author zhoujl
 * @date 2021/4/19 10:35
 * @desc
 */
public interface BaseQueueMsgHandler {

    /**
     * 处理队列中的消息
     *
     * @param taskItem
     */
    void handle(TaskItem taskItem);

}
