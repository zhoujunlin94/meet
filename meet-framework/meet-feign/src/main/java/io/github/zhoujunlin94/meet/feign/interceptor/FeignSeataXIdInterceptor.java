package io.github.zhoujunlin94.meet.feign.interceptor;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * @author zhoujunlin
 * @date 2023年09月23日 15:47
 * @desc
 */
@Component
@ConditionalOnClass(RootContext.class)
public class FeignSeataXIdInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String xid = RootContext.getXID();
        if (StrUtil.isNotBlank(xid)) {
            requestTemplate.header(RootContext.KEY_XID, xid);
        }
    }
}
