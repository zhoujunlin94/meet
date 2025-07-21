package io.github.zhoujunlin94.meet.feign.interceptor;

import cn.hutool.core.date.StopWatch;
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

        MDC.put(RequestIdUtil.REQUEST_ID, request.header(RequestIdUtil.REQUEST_ID));
        StringBuilder logInfo = new StringBuilder("\n|=================[PRINT_REQUEST]======================\n")
                .append("|=> 请求URL: ").append(request.url()).append("\n")
                .append("|=> 请求Method: ").append(request.method()).append("\n")
                .append("|=> 请求Headers:").append(request.headers()).append("\n");

        // 打印请求体（如果有）
        RequestBody requestBody = request.body();
        if (Objects.nonNull(requestBody)) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String body = buffer.readString(StandardCharsets.UTF_8);
            logInfo.append("|=> 请求Body: ").append(body).append("\n");
        }

        // 执行请求
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Response response = chain.proceed(request);
        stopWatch.stop();
        logInfo.append("|=> 请求耗时: ").append(stopWatch.getTotalTimeMillis()).append("ms").append("\n");

        // 打印响应信息
        logInfo.append("|=> 响应Code: ").append(response.code()).append("\n");
        // 不消耗原始流
        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
        logInfo.append("|=> 响应Body: ").append(responseBody.string()).append("\n");
        logInfo.append("|=================[PRINT_REQUEST]======================\n");
        log.warn(logInfo.toString());
        return response;
    }

}
