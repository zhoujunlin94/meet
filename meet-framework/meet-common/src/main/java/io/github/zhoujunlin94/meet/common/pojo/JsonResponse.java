package io.github.zhoujunlin94.meet.common.pojo;

import com.alibaba.fastjson.JSONObject;
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
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private Object data;
    private String msg;

    public static JsonResponse ok() {
        return JsonResponse.builder().code(CommonErrorCode.S_SUC.getCode()).msg(CommonErrorCode.S_SUC.getMsg()).build();
    }

    public static JsonResponse ok(Object data) {
        return JsonResponse.builder().code(CommonErrorCode.S_SUC.getCode()).msg(CommonErrorCode.S_SUC.getMsg()).data(data).build();
    }

    public static JsonResponse fail(CommonErrorCode errorCode) {
        return JsonResponse.builder().code(errorCode.getCode()).msg(errorCode.getMsg()).build();
    }

    public static JsonResponse fail(String msg) {
        return JsonResponse.builder().code(CommonErrorCode.S_FAIL.getCode()).msg(msg).build();
    }

    public static JsonResponse fail(CommonErrorCode errorCode, Object data) {
        return JsonResponse.builder().code(errorCode.getCode()).msg(errorCode.getMsg()).data(data).build();
    }

    public static JsonResponse fail(int code, String msg, Object data) {
        return JsonResponse.builder().code(code).msg(msg).data(data).build();
    }

    public static JsonResponse fail(int code, String msg) {
        return fail(code, msg, null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
