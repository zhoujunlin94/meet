package io.github.zhoujunlin94.meet.feign.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhoujunlin
 * @date 2023年09月23日 15:47
 */
@Slf4j
@Component
@ConditionalOnClass(name = {FeignSeataXIdInterceptor.ROOT_CONTEXT_CLAZZ})
public class FeignSeataXIdInterceptor implements RequestInterceptor {

    public static final String ROOT_CONTEXT_CLAZZ = "io.seata.core.context.RootContext";

    private static final String XID = "TX_XID";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            // 获取XID
            Method getXIDMethod = ReflectUtil.getMethodByName(Class.forName(ROOT_CONTEXT_CLAZZ), "getXID");
            String xid = ReflectUtil.invokeStatic(getXIDMethod);
            if (StrUtil.isNotBlank(xid)) {
                requestTemplate.header(XID, xid);
            }
        } catch (Exception e) {
            log.error("FeignSeataXIdInterceptor传递XID出错", e);
        }
    }

}
