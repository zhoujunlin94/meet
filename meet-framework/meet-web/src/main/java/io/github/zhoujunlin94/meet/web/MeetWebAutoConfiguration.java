package io.github.zhoujunlin94.meet.web;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import io.github.zhoujunlin94.meet.common.constant.CommonConstant;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhoujunlin
 * @date 2023年02月20日 10:37
 * @desc
 */
@EnableAsync
@Configuration
@ComponentScan
public class MeetWebAutoConfiguration {

    /**
     * 当拒绝处理任务时的策略:由主线程处理该任务
     */
    @Primary
    @Bean(CommonConstant.MEET_THREAD_POOL)
    public ThreadPoolTaskExecutor meetThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数  IO密集型 2*n+1  CPU密集型  n+1
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1);
        // 最大线程数 - 核心线程数 = 救急线程数
        executor.setMaxPoolSize(100);
        // 救急线程保活时间
        executor.setKeepAliveSeconds(300);
        // 队列容量  有界  LinkedBlockingQueue
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("meet-pool-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    public HttpMessageConverter<Object> fastJsonHttpMessageConverter() {
        //设置时区
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        FastJsonConfig jsonConfig = new FastJsonConfig();
        //日期格式
        jsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        //特性设置
        jsonConfig.setFeatures(
                //Feature.OrderedField,
                Feature.IgnoreNotMatch
        );
        jsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteBigDecimalAsPlain,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.DisableCircularReferenceDetect
        );
        //字符编码
        jsonConfig.setCharset(StandardCharsets.UTF_8);
        //序列化设置
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        jsonConfig.setSerializeConfig(serializeConfig);
        //反序列化设置
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        //"autoType is not support"问题,使用setAutoTypeSupport=true的全局设置
        parserConfig.setAutoTypeSupport(true);
        jsonConfig.setParserConfig(parserConfig);

        //处理中文乱码
        ArrayList<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.valueOf("text/json;charset=UTF-8"));

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(jsonConfig);

        return fastConverter;
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier,
                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties,
                                                                         Environment environment) {
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();

        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());

        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = webEndpointProperties.getDiscovery().isEnabled() &&
                (StrUtil.isNotBlank(basePath) || ManagementPortType.DIFFERENT.equals(ManagementPortType.get(environment)));
        return new WebMvcEndpointHandlerMapping(
                endpointMapping,
                webEndpoints,
                endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping,
                null
        );
    }

}
