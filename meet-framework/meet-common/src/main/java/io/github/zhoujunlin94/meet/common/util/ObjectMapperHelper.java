package io.github.zhoujunlin94.meet.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import io.github.zhoujunlin94.meet.common.constant.CommonConst;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author zhoujunlin
 * @date 2025/7/24 22:02
 */
@Slf4j
public final class ObjectMapperHelper {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .defaultDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN))
                .build();

        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        OBJECT_MAPPER.registerModule(simpleModule);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    // ================ 序列化 ==================

    public static String toJSONString(Object obj) {
        try {
            if (Objects.nonNull(obj)) {
                return OBJECT_MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.warn("toJSONString失败", e);
        }
        return CommonConst.EMPTY_JSON;
    }

    public static String toPrettyJSONString(Object obj) {
        try {
            if (Objects.nonNull(obj)) {
                return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.warn("toPrettyJSONString失败", e);
        }
        return CommonConst.EMPTY_JSON;
    }

    // ================ 反序列化 ==================

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            if (StrUtil.isNotBlank(json) && Objects.nonNull(clazz)) {
                return OBJECT_MAPPER.readValue(json, clazz);
            }
        } catch (Exception e) {
            log.warn("parseObject失败", e);
        }
        return null;
    }


    public static <T> T parseObject(String json, TypeReference<T> typeRef) {
        try {
            if (StrUtil.isNotBlank(json) && Objects.nonNull(typeRef)) {
                return OBJECT_MAPPER.readValue(json, typeRef);
            }
        } catch (Exception e) {
            log.warn("parseObject失败", e);
        }
        return null;
    }

    public static <T> T parseObject(String source, JavaType javaType) {
        try {
            return OBJECT_MAPPER.readValue(source, javaType);
        } catch (Exception e) {
            log.warn("parseObject失败", e);
        }
        return null;
    }

    public static <T> List<T> parseList(String json, Class<T> elementType) {
        try {
            if (StrUtil.isNotBlank(json) && Objects.nonNull(elementType)) {
                JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
                return OBJECT_MAPPER.readValue(json, javaType);
            }
        } catch (Exception e) {
            log.warn("parseObject失败", e);
        }
        return Lists.newArrayList();
    }

    /**
     * JSON 字符串转 Map
     */
    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> vClass) {
        JavaType javaType = constructParametricType(Map.class, keyClass, vClass);
        return parseObject(json, javaType);
    }

    // ================ 类型转换 ==================

    public static <T> T convert(Object source, Class<T> targetClass) {
        return OBJECT_MAPPER.convertValue(source, targetClass);
    }

    public static <T> T convert(Object source, TypeReference<T> typeReference) {
        return OBJECT_MAPPER.convertValue(source, typeReference);
    }

    public static <T> T convert(Object source, JavaType javaType) {
        return OBJECT_MAPPER.convertValue(source, javaType);
    }

    public static <T> T convert(Object source, Class<?> rawClass, Class<?>... parameterClasses) {
        return OBJECT_MAPPER.convertValue(source, constructParametricType(rawClass, parameterClasses));
    }

    public static JavaType constructParametricType(Class<?> rawClass, Class<?>... parameterClasses) {
        return TypeFactory.defaultInstance()
                .constructParametricType(rawClass, parameterClasses);
    }

}
