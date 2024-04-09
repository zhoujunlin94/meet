package io.github.zhoujunlin94.meet.feign;

import feign.RequestInterceptor;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoujunlin
 * @date 2023年02月20日 10:37
 * @desc
 */
@Configuration
@ComponentScan
public class MeetFeignAutoConfiguration {

    @Bean
    public RequestInterceptor feignRequestIdInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(RequestIdUtil.REQUEST_ID, RequestIdUtil.getRequestId());
        };
    }

}