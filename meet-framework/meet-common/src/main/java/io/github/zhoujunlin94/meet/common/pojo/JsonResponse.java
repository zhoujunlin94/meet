package io.github.zhoujunlin94.meet.common.pojo;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author zhoujl
 * @Date 2020/5/3 19:02
 * @Description json格式响应
 **/
@Data
@Accessors(chain = true)
public class JsonResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;

    public boolean checkSuccess() {
        return ObjectUtil.equal(CommonErrorCode.S_SUCCESS.getCode(), this.code);
    }

    public static JsonResponse<Object> success() {
        return new JsonResponse<Object>().setCode(CommonErrorCode.S_SUCCESS.getCode()).setMessage(CommonErrorCode.S_SUCCESS.getMessage());
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<T>().setCode(CommonErrorCode.S_SUCCESS.getCode()).setMessage(CommonErrorCode.S_SUCCESS.getMessage()).setData(data);
    }

    public static JsonResponse<Object> fail(CommonErrorCode errorCode) {
        return new JsonResponse<Object>().setCode(errorCode.getCode()).setMessage(errorCode.getMessage());
    }

    public static JsonResponse<Object> fail(String msg) {
        return new JsonResponse<Object>().setCode(CommonErrorCode.S_FAIL.getCode()).setMessage(msg);
    }

    public static <T> JsonResponse<T> fail(CommonErrorCode errorCode, T data) {
        return new JsonResponse<T>().setCode(errorCode.getCode()).setMessage(errorCode.getMessage()).setData(data);
    }

    public static <T> JsonResponse<T> create(int code, String msg, T data) {
        return new JsonResponse<T>().setCode(code).setMessage(msg).setData(data);
    }

    public static JsonResponse<Object> create(int code, String msg) {
        return create(code, msg, null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
