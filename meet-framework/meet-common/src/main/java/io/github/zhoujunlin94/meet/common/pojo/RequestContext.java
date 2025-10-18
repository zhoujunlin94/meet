package io.github.zhoujunlin94.meet.common.pojo;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhoujunlin
 * @date 2023年03月14日 17:36
 * @desc
 */
@Data
@Accessors(chain = true)
public class RequestContext {

    private String requestId;

    private Integer userId;

    private String clientIp;

    private JSONObject extendContext;

    public JSONObject getExtendContext() {
        return ObjectUtil.defaultIfNull(this.extendContext, new JSONObject());
    }
}
