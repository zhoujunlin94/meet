package io.github.zhoujunlin94.meet.common.pojo;

import com.alibaba.fastjson.JSONObject;
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

    private static final long serialVersionUID = 1L;

    private int code;
    private T data;
    private String msg;

    public boolean checkSuccess() {
        return CommonErrorCode.S_SUC.getCode() == this.code;
    }

    public static JsonResponse<Object> success() {
        return new JsonResponse<Object>().setCode(CommonErrorCode.S_SUC.getCode()).setMsg(CommonErrorCode.S_SUC.getMsg());
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<T>().setCode(CommonErrorCode.S_SUC.getCode()).setMsg(CommonErrorCode.S_SUC.getMsg()).setData(data);
    }

    public static JsonResponse<Object> fail(CommonErrorCode errorCode) {
        return new JsonResponse<Object>().setCode(errorCode.getCode()).setMsg(errorCode.getMsg());
    }

    public static JsonResponse<Object> fail(String msg) {
        return new JsonResponse<Object>().setCode(CommonErrorCode.S_FAIL.getCode()).setMsg(msg);
    }

    public static <T> JsonResponse<T> fail(CommonErrorCode errorCode, T data) {
        return new JsonResponse<T>().setCode(errorCode.getCode()).setMsg(errorCode.getMsg()).setData(data);
    }

    public static <T> JsonResponse<T> create(int code, String msg, T data) {
        return new JsonResponse<T>().setCode(code).setMsg(msg).setData(data);
    }

    public static JsonResponse<Object> create(int code, String msg) {
        return create(code, msg, null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
