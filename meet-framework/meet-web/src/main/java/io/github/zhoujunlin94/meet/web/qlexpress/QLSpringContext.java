package io.github.zhoujunlin94.meet.web.qlexpress;

import com.alibaba.qlexpress4.runtime.Value;
import com.alibaba.qlexpress4.runtime.context.ExpressContext;
import com.alibaba.qlexpress4.runtime.data.DataValue;
import com.alibaba.qlexpress4.runtime.data.MapItemValue;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2025年12月23日 15:36
 * @desc
 */
@RequiredArgsConstructor
public class QLSpringContext implements ExpressContext {

    private final Map<String, Object> context;
    private final ApplicationContext applicationContext;

    @Override
    public Value get(Map<String, Object> attachments, String variableName) {
        Object value = context.get(variableName);
        if (value != null) {
            return new MapItemValue(context, variableName);
        }
        Object bean = applicationContext.containsBean(variableName) ? applicationContext.getBean(variableName) : null;
        if (bean != null) {
            return new DataValue(bean);
        }
        return new MapItemValue(context, value);
    }
}