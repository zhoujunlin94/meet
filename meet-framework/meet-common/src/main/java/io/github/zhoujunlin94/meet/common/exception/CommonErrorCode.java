package io.github.zhoujunlin94.meet.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhoujl
 * @date 2021/3/31 15:32
 * @desc
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    /**
     * 错误码描述
     * S开头 系统错误信息
     * P开头 参数错误信息
     * B开头 业务错误信息
     */
    S_SUCCESS(0, "success"),
    S_FAIL(1, "失败"),
    S_SYSTEM_BUSY(2, "系统繁忙，请稍后再试!"),
    S_OVER_CALLED(3, "接口访问超出频率限制"),

    P_BAD_PARAMETER(1001, "参数错误"),
    P_PARAM_CHECK_ERROR(1002, "参数校验出错"),

    B_UN_LOGIN(2001, "未登录"),
    B_UN_AUTHORIZATION(2002, "未授权"),
    B_DATA_NOT_FOUND(2003, "数据不存在"),
    B_AUTHORIZATION_FAILED(2004, "授权失败"),


    B_USER_NAME_NOT_FOUND(3001, "用户不存在"),
    B_USER_LOCK(3002, "用户被冻结"),
    B_PASSWORD_ERROR(3003, "用户名或密码不正确"),

    ;

    private final int code;
    private final String message;

}
