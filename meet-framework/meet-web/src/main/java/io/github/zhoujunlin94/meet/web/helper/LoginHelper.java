package io.github.zhoujunlin94.meet.web.helper;

import io.github.zhoujunlin94.meet.common.util.RequestContextUtil;

import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2024-07-08-17:13
 */
public class LoginHelper {

    public static boolean isLogin() {
        Integer userId = RequestContextUtil.get().getUserId();
        return Objects.nonNull(userId) && userId > 0;
    }

    public static Integer getUserId() {
        return RequestContextUtil.get().getUserId();
    }

}
