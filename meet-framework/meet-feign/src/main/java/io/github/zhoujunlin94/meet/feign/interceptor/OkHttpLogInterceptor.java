package io.github.zhoujunlin94.meet.feign.interceptor;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.MDC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025-07-21 15:18
 */
@Slf4j
public class OkHttpLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String requestId = StrUtil.blankToDefault(request.header(RequestIdUtil.REQUEST_ID), RequestIdUtil.getRequestId());
        MDC.put(RequestIdUtil.REQUEST_ID, requestId);
        log.warn(">>>请求URL: {} {}", request.method(), request.url());
        log.warn(">>>请求Headers: \n{}", request.headers());

        // 打印请求体（如果有）
        RequestBody requestBody = request.body();
        if (Objects.nonNull(requestBody)) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String body = buffer.readString(StandardCharsets.UTF_8);
            log.warn(">>>请求Body: {}", body);
        }

        // 执行请求
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Response response = chain.proceed(request);
        stopWatch.stop();
        log.warn("<<<请求耗时: {}ms", stopWatch.getTotalTimeMillis());

        // 打印响应信息
        log.warn("<<<响应code: {}", response.code());
        // 不消耗原始流
        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
        log.warn("<<<响应body: \n{}", responseBody.string());
        return response;
    }

}
