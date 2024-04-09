package io.github.zhoujunlin94.meet.redis.handler;

/**
 * @author zhoujl
 * @date 2021/4/19 10:35
 * @desc
 */
public interface BasePubSubMsgHandler {

    /**
     * 处理redis发布订阅模式的消息
     *
     * @param channel 通道
     * @param msg     消息
     */
    void handle(String channel, String msg);

}
