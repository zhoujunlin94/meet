package io.github.zhoujunlin94.meet.redis.consumer;

import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.redis.dispatcher.PubSubMsgDispatcher;
import io.github.zhoujunlin94.meet.redis.queue.TaskItem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;

/**
 * @author zhoujl
 * @date 2021/4/19 18:01
 * @desc
 */
@Slf4j
public class RedisPubSubMsgConsumer implements MessageListener {

    @Resource
    private PubSubMsgDispatcher pubSubDispatcher;

    @Override
    public void onMessage(@NonNull Message message, byte[] bytes) {
        try {
            String msg = new String(message.getBody());
            String channel = new String(message.getChannel());
            TaskItem taskItem = JSONObject.parseObject(msg, TaskItem.class);
            MDC.put(RequestIdUtil.REQUEST_ID, taskItem.getRequestId() + "_RedisPubSub");
            log.warn("RedisPubSubMsgConsumer#onMessage, channel:{}, msg:{}", channel, msg);
            pubSubDispatcher.dispatch(channel, msg);
        } catch (Exception e) {
            log.error("分发发布订阅消息出错", e);
        } finally {
            MDC.remove(RequestIdUtil.REQUEST_ID);
        }
    }

}
