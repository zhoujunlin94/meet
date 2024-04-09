package io.github.zhoujunlin94.meet.redis.dispatcher;

import cn.hutool.extra.spring.SpringUtil;
import io.github.zhoujunlin94.meet.redis.handler.BasePubSubMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujl
 * @date 2021/4/19 10:33
 * @desc
 */
@Slf4j
public class PubSubMsgDispatcher implements InitializingBean {

    private Map<String, BasePubSubMsgHandler> pubSubHandlerMap;

    @Override
    public void afterPropertiesSet() {
        pubSubHandlerMap = SpringUtil.getBeansOfType(BasePubSubMsgHandler.class);
    }

    public void dispatch(String channel, String msg) {
        try {
            BasePubSubMsgHandler msgHandler = pubSubHandlerMap.get(channel);
            if (Objects.isNull(msgHandler)) {
                log.error("未找到对应通道{}处理器,消息:{}", channel, msg);
                return;
            }
            msgHandler.handle(channel, msg);
        } catch (Exception e) {
            log.error("分发消息出错", e);
        }
    }

}
