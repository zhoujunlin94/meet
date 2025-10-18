package io.github.zhoujunlin94.meet.redis.queue;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhoujl
 * @date 2021/3/5 14:52
 * @desc 队列消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskItem {

    private String requestId;

    private String id;

    private String handlerName;

    private Object msg;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
