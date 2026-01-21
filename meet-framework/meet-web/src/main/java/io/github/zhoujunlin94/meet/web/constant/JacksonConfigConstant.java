package io.github.zhoujunlin94.meet.web.constant;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zhoujunlin94.meet.common.util.ObjectMapperHelper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author zhoujunlin
 * @date 2025/7/25 21:14
 */
public class JacksonConfigConstant {

    public static HttpMessageConverter<Object> defaultJacksonHttpMessageConverter() {
        ObjectMapper objectMapper = ObjectMapperHelper.getObjectMapper();
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
