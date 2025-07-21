package io.github.zhoujunlin94.meet.feign;

import feign.Client;
import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.feign.interceptor.OkHttpLogInterceptor;
import okhttp3.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2023年02月20日 10:37
 * @desc
 */
@Configuration
@ComponentScan
public class MeetFeignAutoConfiguration {

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
                .addInterceptor(new OkHttpLogInterceptor())
                .retryOnConnectionFailure(true)
                .build();
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