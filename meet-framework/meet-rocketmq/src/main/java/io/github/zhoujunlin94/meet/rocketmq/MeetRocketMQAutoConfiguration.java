package io.github.zhoujunlin94.meet.rocketmq;

import io.github.zhoujunlin94.meet.rocketmq.aspect.RocketMQMessageListenerAspect;
import io.github.zhoujunlin94.meet.rocketmq.helper.RocketMQHelper;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoujunlin
 * @date 2023年02月20日 10:37
 * @desc
 */
@Configuration
@AutoConfigureAfter(RocketMQAutoConfiguration.class)
public class MeetRocketMQAutoConfiguration {

    @Bean
    public RocketMQHelper rocketMQHelper(@Autowired RocketMQTemplate rocketMQTemplate) {
        return new RocketMQHelper(rocketMQTemplate);
    }

    @Bean
    public RocketMQMessageListenerAspect rocketMQMessageListenerAspect() {
        return new RocketMQMessageListenerAspect();
    }


}
