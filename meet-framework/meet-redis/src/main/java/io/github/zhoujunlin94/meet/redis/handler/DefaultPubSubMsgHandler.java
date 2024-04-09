package io.github.zhoujunlin94.meet.redis.handler;

import io.github.zhoujunlin94.meet.redis.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhoujunlin
 * @date 2022/1/1
 * @desc
 **/
@Slf4j
@Component(RedisConstant.BeanName.DEFAULT_PUB_SUB_HANDLER)
public class DefaultPubSubMsgHandler implements BasePubSubMsgHandler {

    @Override
    public void handle(String channel, String msg) {
        log.info("DefaultPubSubHandler->当前channel:{},接收到消息:{}", channel, msg);
    }

}
