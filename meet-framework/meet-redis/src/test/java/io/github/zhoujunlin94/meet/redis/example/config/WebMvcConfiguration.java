package io.github.zhoujunlin94.meet.redis.example.config;

import io.github.zhoujunlin94.meet.web.constant.FastJsonConfigConst;
import io.github.zhoujunlin94.meet.web.interceptor.HttpBaseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhoujl
 * @date 2021/4/22 18:18
 * @desc
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private HttpBaseInterceptor httpBaseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpBaseInterceptor)
                .excludePathPatterns("/favicon.ico");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, FastJsonConfigConst.defaultFastJsonHttpMessageConverter());
    }

}

