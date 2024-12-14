package io.github.zhoujunlin94.meet.redis.helper;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2023年12月12日 15:03
 * @desc
 */
@Slf4j
@Component
@ConditionalOnBean(RedissonClient.class)
public class RedissonHelper {

    private static RedissonClient redissonClient;

    public RedissonHelper(@Autowired RedissonClient redissonClient) {
        RedissonHelper.redissonClient = redissonClient;
    }

    public static boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    public static boolean tryLock(String lockKey, long waitTime, TimeUnit timeUnit) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(waitTime, timeUnit);
        } catch (Exception e) {
            log.error("RedissonHelper.tryLock error", e);
        }
        return false;
    }

    public static boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (Exception e) {
            log.error("RedissonHelper.tryLock error", e);
        }
        return false;
    }

    public static void unLock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("RedissonHelper.unLock", e);
        }
    }

    /**
     * 滑动窗口实现限流
     *
     * @param limiterKey   限流key
     * @param rateType     限流类型
     * @param rate         令牌个数
     * @param rateInterval 产生指定令牌个数的时间间隔
     * @param intervalUnit 时间间隔单位
     * @return 语义：每1秒内产生2个令牌
     */
    public static boolean trySetRate(String limiterKey, RateType rateType, long rate, long rateInterval, RateIntervalUnit intervalUnit) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterKey);
        return rateLimiter.trySetRate(rateType, rate, rateInterval, intervalUnit);
    }

    /**
     * 取一个令牌
     *
     * @param limiterKey
     * @return
     */
    public static boolean tryAcquire(String limiterKey) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiterKey);
        return rateLimiter.tryAcquire();
    }

    public static RDelayedQueue<String> getDelayedQueue(String queueName) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(queueName);
        return redissonClient.getDelayedQueue(blockingDeque);
    }

    public static void offer(String queueName, Object payload, long delay, TimeUnit timeUnit) {
        String msg = payload instanceof String ? payload.toString() : JSONObject.toJSONString(payload);
        getDelayedQueue(queueName).offer(msg, delay, timeUnit);
    }

}
