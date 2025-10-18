package io.github.zhoujunlin94.meet.redis.queue;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import io.github.zhoujunlin94.meet.redis.dispatcher.QueueMsgDispatcher;
import io.github.zhoujunlin94.meet.redis.helper.RedissonHelper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 */
@Slf4j
public class RedissonDelayedQueue implements InitializingBean {

    @Resource
    private QueueMsgDispatcher queueMsgDispatcher;


    public void delay(String handlerName, Object msg, long delayTime, TimeUnit timeUnit) {
        TaskItem task = new TaskItem(RequestIdUtil.getRequestId(), IdUtil.fastSimpleUUID(), handlerName, msg);
        RedissonHelper.offer(RedisConstant.DEFAULT_DELAYED_QUEUE_NAME, task.toString(), delayTime, timeUnit);
    }


    @Override
    public void afterPropertiesSet() {
        ThreadUtil.newExecutor().execute(this::start);
        log.warn("redisson delayed queue started...");
    }

    public void start() {
        try {
            RBlockingQueue<String> blockingQueue = RedissonHelper.getBlockingQueue(RedisConstant.DEFAULT_DELAYED_QUEUE_NAME);
            while (!Thread.interrupted()) {
                String msg = blockingQueue.poll(500, TimeUnit.MILLISECONDS);
                if (StrUtil.isNotBlank(msg)) {
                    TaskItem task = JSONObject.parseObject(msg, TaskItem.class);
                    queueMsgDispatcher.dispatch(task);
                }
            }
        } catch (Exception e) {
            log.error("获取延迟消息出错", e);
        }
    }

}