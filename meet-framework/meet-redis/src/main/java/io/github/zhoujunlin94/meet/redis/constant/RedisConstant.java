package io.github.zhoujunlin94.meet.redis.constant;

import cn.hutool.core.io.FileUtil;

/**
 * @author zhoujunlin
 * @date 2022/3/12 21:26
 */
public final class RedisConstant {

    public static final String DEFAULT_DELAYED_QUEUE_NAME = "default_delayed_queue";

    public static class LuaScripts {

        public static final String LIMITER_LUA = FileUtil.readUtf8String("classpath:scripts/limiter.lua");

        public static final String QUEUE_LUA = FileUtil.readUtf8String("classpath:scripts/queue.lua");

    }

}
