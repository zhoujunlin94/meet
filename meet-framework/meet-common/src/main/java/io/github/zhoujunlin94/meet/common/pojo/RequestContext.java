package io.github.zhoujunlin94.meet.common.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhoujunlin
 * @date 2023年03月14日 17:36
 * @desc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    private String requestId;

    private Integer userId;

    private String clientIP;

    @Builder.Default
    private JSONObject extJSONObject = new JSONObject();

}
