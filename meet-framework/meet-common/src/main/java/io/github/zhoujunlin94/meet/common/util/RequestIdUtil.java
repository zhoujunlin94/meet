package io.github.zhoujunlin94.meet.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.pojo.RequestContext;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * @author zhoujl
 * @date 2022/3/12 15:41
 * @desc
 */
public final class RequestIdUtil {

    public static final String REQUEST_ID = "X-REQUEST-ID";

    public static String generateRequestId() {
        return IdUtil.fastSimpleUUID();
    }

    public static String getRequestId() {
        String requestId = MDC.get(REQUEST_ID);
        if (StrUtil.isBlank(requestId)) {
            RequestContext requestContext = RequestContextUtil.get();
            if (Objects.nonNull(requestContext)) {
                requestId = requestContext.getRequestId();
            }
        }

        if (StrUtil.isBlank(requestId)) {
            requestId = generateRequestId();
        }
        return requestId;
    }

}
