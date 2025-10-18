package io.github.zhoujunlin94.meet.web.constant;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.TimeZone;

/**
 * @author zhoujunlin
 * @date 2025-04-14-15:25
 */
public class FastJsonConfigConst {

    public static FastJsonConfig defaultFastJsonConfig() {
        // 设置默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 设置日期格式
        fastJsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        // 设置字符编码
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        // 设置序列化特性
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.SortField,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastJsonConfig.setFeatures(
                Feature.IgnoreNotMatch,
                Feature.OrderedField
        );

        ParserConfig globalInstance = ParserConfig.getGlobalInstance();
        globalInstance.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        globalInstance.setAutoTypeSupport(true);
        fastJsonConfig.setParserConfig(globalInstance);
        return fastJsonConfig;
    }

    public static HttpMessageConverter<Object> defaultFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        fastConverter.setFastJsonConfig(defaultFastJsonConfig());
        return fastConverter;
    }

}
