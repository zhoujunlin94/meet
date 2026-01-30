package io.github.zhoujunlin94.meet.web.helper;

import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.common.pojo.RequestContext;
import io.github.zhoujunlin94.meet.common.util.RequestContextUtil;

import java.util.Optional;

/**
 * @author zhoujunlin
 * @date 2024-07-08-17:13
 */
public class LoginHelper {

    public static boolean isLogin() {
        String userId = Optional.ofNullable(RequestContextUtil.get()).map(RequestContext::getUserId).orElse(null);
        return StrUtil.isNotBlank(userId);
    }

    public static String getUserId() {
        String userId = Optional.ofNullable(RequestContextUtil.get()).map(RequestContext::getUserId).orElse(null);
        if (StrUtil.isBlank(userId)) {
            throw MeetException.meet(CommonErrorCode.B_UN_LOGIN);
        }
        return userId;
    }

}
