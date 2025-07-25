package io.github.zhoujunlin94.meet.common.util;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
 * Spring AOP 工具类
 */
public class SpringAopUtil {

    /**
     * 获取代理的目标对象
     *
     * @param proxy 代理对象
     * @return 目标对象
     */
    public static Object getTarget(Object proxy) throws Exception {
        // 不是代理对象
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        // Jdk 代理
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        }
        // Cglib 代理
        return getCglibProxyTargetObject(proxy);
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Object dynamicAdvisedInterceptor = BeanUtil.getFieldValue(proxy, "CGLIB$CALLBACK_0");
        AdvisedSupport advisedSupport = (AdvisedSupport) BeanUtil.getFieldValue(dynamicAdvisedInterceptor, "advised");
        return advisedSupport.getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        AopProxy aopProxy = (AopProxy) BeanUtil.getFieldValue(proxy, "h");
        AdvisedSupport advisedSupport = (AdvisedSupport) BeanUtil.getFieldValue(aopProxy, "advised");
        return advisedSupport.getTargetSource().getTarget();
    }

}
