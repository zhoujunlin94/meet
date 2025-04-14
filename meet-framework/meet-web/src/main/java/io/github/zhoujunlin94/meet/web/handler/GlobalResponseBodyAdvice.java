package io.github.zhoujunlin94.meet.web.handler;

import cn.hutool.core.collection.CollUtil;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.pojo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * @author zhoujunlin
 */
@Slf4j
@RestControllerAdvice(
        annotations = {RestController.class}
)
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        JsonResponse jsonResponse = body instanceof JsonResponse ? (JsonResponse) body : JsonResponse.builder().code(CommonErrorCode.S_SUC.getCode()).
                msg(CommonErrorCode.S_SUC.getMsg()).data(body).build();
        Object result = MediaType.APPLICATION_JSON.equals(selectedContentType) || MediaType.APPLICATION_JSON_UTF8.equals(selectedContentType)
                ? jsonResponse : body;
        log.warn("当前接口响应内容:{}", result);
        return result;
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        String executorClazzName = returnType.getExecutable().getDeclaringClass().getName();
        List<String> notSupportClazz = CollUtil.newArrayList(
                "springfox.documentation.swagger2.web.Swagger2ControllerWebMvc",
                "springfox.documentation.swagger.web.ApiResourceController",
                "org.springdoc.webmvc.ui.SwaggerConfigResource"
        );
        return !CollUtil.contains(notSupportClazz, executorClazzName);
    }

}
