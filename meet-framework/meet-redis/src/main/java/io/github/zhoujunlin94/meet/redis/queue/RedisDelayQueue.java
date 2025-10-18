package io.github.zhoujunlin94.meet.redis.queue;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import io.github.zhoujunlin94.meet.redis.dispatcher.QueueMsgDispatcher;
import io.github.zhoujunlin94.meet.redis.helper.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujl
 * @date 2021/3/5 14:51
 * @desc
 */
@Slf4j
@Deprecated
public class RedisDelayQueue implements InitializingBean {

    private static final String QUEUE_KEY = "queue:msg";

    @Resource
    private QueueMsgDispatcher queueMsgDispatcher;


    /**
     * 向队列中添加消息
     *
     * @param handlerName 处理器名字
     * @param msg         消息内容
     * @param delayTime   延迟执行时间
     * @param timeUnit    延迟时间单位
     */
    public void delay(String handlerName, Object msg, long delayTime, TimeUnit timeUnit) {
        TaskItem task = new TaskItem(RequestIdUtil.getRequestId(), IdUtil.fastSimpleUUID(), handlerName, msg);
        RedisHelper.addZSet(QUEUE_KEY, task.toString(), (System.currentTimeMillis() + timeUnit.toMillis(delayTime)));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadUtil.newExecutor().execute(this::start);
        log.warn("redis queue started...");
    }

    public void start() {
        try {
            String luaStr = RedisConstant.LuaScripts.QUEUE_LUA;
            while (!Thread.interrupted()) {
                String nowStamp = System.currentTimeMillis() + "";
                List<String> msgList = (List<String>) RedisHelper.execute(luaStr, Collections.singletonList(QUEUE_KEY), Collections.singletonList(nowStamp), List.class);
                if (CollUtil.isEmpty(msgList)) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                    continue;
                }
                msgList.forEach(msgStr -> {
                    TaskItem task = JSONObject.parseObject(msgStr, TaskItem.class);
                    queueMsgDispatcher.dispatch(task);
                });
            }
        } catch (Exception e) {
            log.error("获取延迟消息出错", e);
        }
    }

}