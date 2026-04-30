package io.github.zhoujunlin94.meet.feign;

import cn.hutool.core.util.StrUtil;
import feign.Client;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.okhttp.OkHttpClient;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.feign.interceptor.OkHttpLogInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
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

    @Resource
    private ObjectFactory<HttpMessageConverters> httpMessageConverters;

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
    public Encoder feignEncoder() {
        return new SpringFormEncoder(new SpringEncoder(httpMessageConverters));
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(this.httpMessageConverters));
    }

    @Bean
    public RequestInterceptor feignRequestIdInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(RequestIdUtil.REQUEST_ID, RequestIdUtil.getRequestId());
        };
    }

}