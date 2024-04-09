package io.github.zhoujunlin94.meet.web.interceptor;

import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.pojo.JsonResponse;
import io.github.zhoujunlin94.meet.common.util.ServletUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: zhoujl
 * <p>
 * 拦截器的基类
 */
public abstract class BaseInterceptor implements HandlerInterceptor {

    protected void failed(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        JsonResponse jsonResponse = JsonResponse.builder().code(CommonErrorCode.S_SYSTEM_BUSY.getCode()).msg(message).build();
        ServletUtils.writeJSON(response, jsonResponse);
    }

    protected void fail(HttpServletResponse response, String redirectUrl) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        JsonResponse jsonResponse = JsonResponse.builder().code(CommonErrorCode.S_SYSTEM_BUSY.getCode()).data(redirectUrl).build();
        ServletUtils.writeJSON(response, jsonResponse);
    }
}
