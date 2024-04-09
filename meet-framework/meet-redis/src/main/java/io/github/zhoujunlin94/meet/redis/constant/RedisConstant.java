package io.github.zhoujunlin94.meet.redis.constant;

/**
 * @author zhoujl
 * @date 2022/3/12 21:26
 * @desc
 */
public final class RedisConstant {

    public static class LuaScripts {

        public static final String LIMITER_LUA = "local c = redis.call('get',KEYS[1]) " +
                "if c and tonumber(c) > tonumber(ARGV[1]) then " +
                "return c; " +
                "end " +
                "c = redis.call('incr',KEYS[1]) " +
                "if tonumber(c) == 1 then " +
                "redis.call('expire',KEYS[1],ARGV[2]) " +
                "end " +
                "return c;";

        public static final String QUEUE_LUA = " local result = redis.call('zrangebyscore', KEYS[1], 0, ARGV[1], 'LIMIT', 0, 1) \n" +
                " if next(result) ~= nil and #result > 0 then \n" +
                "    local re = redis.call('zrem', KEYS[1], unpack(result)); \n" +
                "    if re > 0 then \n" +
                "        return result; \n" +
                "    end \n" +
                "    return {} \n" +
                " else \n" +
                "    return {} \n" +
                " end \n";
    }

    public static class BeanName {
        public static final String DEFAULT_QUEUE_HANDLER = "defaultQueueHandler";
        public static final String DEFAULT_PUB_SUB_HANDLER = "defaultPubSubHandler";
        public static final String REDIS_HELPER = "redisHelper";
        public static final String MEET_REDIS_TEMPLATE = "meetRedisTemplate";
    }

}
