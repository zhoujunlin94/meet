package io.github.zhoujunlin94.meet.redis.example.controller;

import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.redis.annotation.Limiter;
import io.github.zhoujunlin94.meet.redis.annotation.RedissonLocker;
import io.github.zhoujunlin94.meet.redis.helper.RedisHelper;
import io.github.zhoujunlin94.meet.redis.queue.RedisDelayQueue;
import io.github.zhoujunlin94.meet.redis.queue.RedissonDelayedQueue;
import io.github.zhoujunlin94.meet.redis.queue.TaskItem;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2023年03月26日 21:37
 * @desc
 */
@RestController
public class RedisMsgController {

    private RedisDelayQueue redisDelayQueue;

    @Resource
    private RedissonDelayedQueue redissonDelayedQueue;

    @GetMapping("/provideMsg")
    public String provider() {
        //验证redis 生产消费模式  起多台服务测试  - 一条消息只有一台机器消费（竞争关系）
        //需要立即被消费
        redisDelayQueue.delay("defaultQueueMsgHandler", "provider demo msg1", 0, TimeUnit.SECONDS);
        //延迟5秒消费
        redisDelayQueue.delay("defaultQueueMsgHandler", "provider demo msg2", 5, TimeUnit.SECONDS);
        redisDelayQueue.delay("defaultQueueMsgHandler", "provider demo msg3", 30, TimeUnit.SECONDS);
        return "success";
    }

    @GetMapping("/provideMsg2")
    public String provider2() {
        //验证redis 生产消费模式  起多台服务测试  - 一条消息只有一台机器消费（竞争关系）
        //需要立即被消费
        redissonDelayedQueue.delay("defaultQueueMsgHandler", "provider demo msg1", 0, TimeUnit.SECONDS);
        //延迟5秒消费
        redissonDelayedQueue.delay("defaultQueueMsgHandler", "provider demo msg2", 5, TimeUnit.SECONDS);
        redissonDelayedQueue.delay("defaultQueueMsgHandler", "provider demo msg3", 30, TimeUnit.SECONDS);
        return "success";
    }

    @GetMapping("/publishMsg")
    public String publishMsg() {
        //验证redis 发布订阅模式  起多台服务测试  - 一条消息所有机器会消费
        TaskItem taskItem = TaskItem.builder().requestId(RequestIdUtil.getRequestId()).msg("msg111111111").build();
        RedisHelper.publishMsg("defaultPubSubMsgHandler", taskItem.toString());
        taskItem.setMsg("msg2222222");
        RedisHelper.publishMsg("defaultPubSubMsgHandler", taskItem.toString());
        return "success";
    }

    @GetMapping("/limit")
    @Limiter(name = "testLimiter", key = "TestLimiterKey3", period = 1000, count = 1)
    public String limit() {
        return "success";
    }

    @GetMapping(value = "/lock", produces = MediaType.APPLICATION_JSON_VALUE)
    @RedissonLocker(prefix = "test", suffixEL = "#name", waitTime = 3)
    public String lock(@RequestParam String name) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5L);
        return "success";
    }


}
