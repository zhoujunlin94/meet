package io.github.zhoujunlin94.meet.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.feign.interceptor.OkHttpLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhoujunlin
 * @date 2025年11月13日 15:20
 * @desc
 */
@Slf4j
public class OkHttpClientHelper {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");

    private static final OkHttpClient CLIENT;

    static {
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new OkHttpLogInterceptor())
                .build();
    }

    // ========== GET ==========

    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        String fullUrl = buildUrl(url, params);
        headers = ObjectUtil.defaultIfNull(headers, Map.of());
        Request request = new Request.Builder().url(fullUrl).get().headers(Headers.of(headers)).build();
        return call(request);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, params, Map.of());
    }

    public static String get(String url) {
        return get(url, Map.of());
    }

    // ========== POST ==========

    public static String post(String url, Map<String, String> params, JSONObject json, Map<String, String> headers) {
        url = buildUrl(url, params);
        json = ObjectUtil.defaultIfNull(json, new JSONObject());
        RequestBody body = RequestBody.create(json.toJSONString(), JSON);
        headers = ObjectUtil.defaultIfNull(headers, Map.of());
        Request request = new Request.Builder().url(url).post(body).headers(Headers.of(headers)).build();
        return call(request);
    }

    public static String post(String url, JSONObject json, Map<String, String> headers) {
        return post(url, Map.of(), json, headers);
    }

    public static String post(String url, JSONObject json) {
        return post(url, Map.of(), json, Map.of());
    }


    // ========== 私有 ==========
    private static String call(Request request) {
        try (Response response = CLIENT.newCall(request).execute()) {
            return bodyString(response);
        } catch (Exception e) {
            log.error("请求失败", e);
            throw new RuntimeException(e);
        }
    }

    private static String bodyString(Response response) {
        try {
            if (Objects.isNull(response) || !response.isSuccessful()) {
                return StrUtil.EMPTY;
            }

            ResponseBody peekBody = response.peekBody(Long.MAX_VALUE);
            return peekBody.string();
        } catch (Exception e) {
            log.error("获取响应体失败", e);
            return StrUtil.EMPTY;
        }
    }

    private static String buildUrl(String url, Map<String, String> params) {
        if (CollUtil.isEmpty(params)) {
            return url;
        }

        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));
        if (url.contains("?")) {
            return url + "&" + query;
        } else {
            return url + "?" + query;
        }
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }

}

