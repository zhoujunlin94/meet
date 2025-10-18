package io.github.zhoujunlin94.meet.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.github.zhoujunlin94.meet.common.pojo.RequestContext;
import io.github.zhoujunlin94.meet.common.util.RequestContextUtil;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import io.github.zhoujunlin94.meet.common.util.ServletUtils;
import io.github.zhoujunlin94.meet.web.constant.WebConstant;
import io.github.zhoujunlin94.meet.web.helper.ProjectHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoujl
 * @date 2021/4/22 18:09
 * @desc
 */
@Slf4j
@Lazy
@Component
public class HttpBaseInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader(WebConstant.X_REQUEST_ID);
        if (StrUtil.isBlank(requestId)) {
            requestId = response.getHeader(WebConstant.X_REQUEST_ID);
        }
        if (StrUtil.isBlank(requestId)) {
            requestId = RequestIdUtil.generateRequestId();
        }
        Integer userId = Convert.toInt(request.getHeader(WebConstant.X_GATEWAY_UID));
        RequestContext requestContext = new RequestContext().setRequestId(requestId)
                .setUserId(userId).setClientIp(ServletUtils.getClientIP());
        RequestContextUtil.set(requestContext);

        MDC.put(WebConstant.X_REQUEST_ID, requestId);
        response.addHeader(WebConstant.X_REQUEST_ID, requestId);

        log.warn("开始访问:{} {}", request.getMethod(), request.getRequestURL().toString());
        log.warn("clientIp:{}", requestContext.getClientIp());
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        if (CollUtil.isNotEmpty(headers)) {
            log.warn("headers:{}", JSON.toJSONString(headers));
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollUtil.isNotEmpty(parameterMap)) {
            log.warn("parameterMap:{}", JSON.toJSONString(parameterMap));
        }

        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("ctx", ProjectHelper.getProjectBasePath(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            long start = (long) request.getAttribute("startTime");
            long end = System.currentTimeMillis();
            log.warn("结束加载请求:{},总用时:{}ms", request.getRequestURL().toString(), end - start);
        } finally {
            try {
                MDC.remove(WebConstant.X_REQUEST_ID);
            } catch (Exception ignore) {
            }
            try {
                RequestContextUtil.remove();
            } catch (Exception ignore) {

            }
        }
    }

}
