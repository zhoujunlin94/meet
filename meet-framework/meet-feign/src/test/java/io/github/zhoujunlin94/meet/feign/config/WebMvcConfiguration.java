package io.github.zhoujunlin94.meet.feign.config;

import io.github.zhoujunlin94.meet.web.interceptor.HttpBaseInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        // 基础http请求拦截器
        registry.addInterceptor(httpBaseInterceptor)
                .excludePathPatterns("/favicon.ico", "/assets/**/*", "/**/*.js", "/**/*.html", "/**/*.css")
                .excludePathPatterns("/swagger-resources", "/v3/api-docs/**/*")
        ;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/doc.html");
    }

}

