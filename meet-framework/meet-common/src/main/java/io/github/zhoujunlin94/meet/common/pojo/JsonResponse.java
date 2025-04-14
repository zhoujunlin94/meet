package io.github.zhoujunlin94.meet.common.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zhoujl
 * @Date 2020/5/3 19:02
 * @Description json格式响应
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JsonResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private T data;
    private String msg;

    @JSONField(deserialize = false, serialize = false)
    public boolean isOk() {
        return CommonErrorCode.S_SUC.getCode() == this.code;
    }

    public static JsonResponse<Object> ok() {
        return JsonResponse.builder().code(CommonErrorCode.S_SUC.getCode()).msg(CommonErrorCode.S_SUC.getMsg()).build();
    }

    public static <T> JsonResponse<T> ok(T data) {
        return JsonResponse.<T>builder().code(CommonErrorCode.S_SUC.getCode()).msg(CommonErrorCode.S_SUC.getMsg()).data(data).build();
    }

    public static JsonResponse<Object> fail(CommonErrorCode errorCode) {
        return JsonResponse.builder().code(errorCode.getCode()).msg(errorCode.getMsg()).build();
    }

    public static JsonResponse<Object> fail(String msg) {
        return JsonResponse.builder().code(CommonErrorCode.S_FAIL.getCode()).msg(msg).build();
    }

    public static <T> JsonResponse<T> fail(CommonErrorCode errorCode, T data) {
        return JsonResponse.<T>builder().code(errorCode.getCode()).msg(errorCode.getMsg()).data(data).build();
    }

    public static <T> JsonResponse<T> fail(int code, String msg, T data) {
        return JsonResponse.<T>builder().code(code).msg(msg).data(data).build();
    }

    public static JsonResponse<Object> fail(int code, String msg) {
        return fail(code, msg, null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
