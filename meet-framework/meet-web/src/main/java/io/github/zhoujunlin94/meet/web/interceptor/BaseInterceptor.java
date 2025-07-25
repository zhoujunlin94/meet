package io.github.zhoujunlin94.meet.web.interceptor;

import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.pojo.JsonResponse;
import io.github.zhoujunlin94.meet.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * @author: zhoujl
 * <p>
 * 拦截器的基类
 */
public abstract class BaseInterceptor implements HandlerInterceptor {

    protected void failed(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");

        JsonResponse<Object> jsonResponse = JsonResponse.fail(message);
        ServletUtils.writeJSON(response, jsonResponse);
    }

    protected void fail(HttpServletResponse response, String redirectUrl) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        JsonResponse<String> jsonResponse = JsonResponse.create(CommonErrorCode.S_FAIL.getCode(), CommonErrorCode.S_FAIL.getMsg(), redirectUrl);
        ServletUtils.writeJSON(response, jsonResponse);
    }
}
