package io.github.zhoujunlin94.meet.common.constant;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;
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
        fastJsonConfig.setWriterFeatures(
                JSONWriter.Feature.PrettyFormat,
                JSONWriter.Feature.WriteBigDecimalAsPlain,
                JSONWriter.Feature.WriteMapNullValue,
                JSONWriter.Feature.WriteEnumUsingToString,
                JSONWriter.Feature.ReferenceDetection
        );

        // 设置反序列化特性
        fastJsonConfig.setReaderFeatures(
                JSONReader.Feature.IgnoreAutoTypeNotMatch,
                JSONReader.Feature.SupportSmartMatch
        );

        return fastJsonConfig;
    }
    
    public static HttpMessageConverter<Object> defaultFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        fastConverter.setFastJsonConfig(defaultFastJsonConfig());
        return fastConverter;
    }

}
