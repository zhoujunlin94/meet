package io.github.zhoujunlin94.meet.feign;

import cn.hutool.core.util.StrUtil;
import feign.Client;
import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.feign.interceptor.OkHttpLogInterceptor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${log.feign.style:interceptor}")
    private String feignLogStyle;

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        okhttp3.OkHttpClient.Builder okHttpClientBuilder = new okhttp3.OkHttpClient.Builder();
        if (StrUtil.equals(feignLogStyle, "interceptor")) {
            okHttpClientBuilder.addInterceptor(new OkHttpLogInterceptor());
        }

        return okHttpClientBuilder.build();
    }

    @Bean
    public Client feignClient() {
        return new OkHttpClient(okHttpClient());
    }

    @Bean
    public RequestInterceptor feignRequestIdInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(RequestIdUtil.REQUEST_ID, RequestIdUtil.getRequestId());
        };
    }

}