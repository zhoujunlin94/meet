package io.github.zhoujunlin94.meet.web.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2025年12月16日 16:22
 * @desc
 */
@Component
@RequiredArgsConstructor
public class BeanMethodReflectInvoker {
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public Object invoke(Object target, String methodName, Map<String, Object> paramMap) {
        Assert.hasText(methodName, "方法名不能为空, methodName: " + methodName);

        Class<?> targetClass = AopUtils.getTargetClass(target);
        Method method = findBestMethod(targetClass, methodName, paramMap);
        Object[] args = buildArgs(method, paramMap);
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("反射调用失败", e);
        }
    }


    public Object invoke(String beanName, String methodName, Map<String, Object> paramMap) {
        Object target = applicationContext.getBean(beanName);
        Assert.notNull(target, "Bean不存在, beanName: " + beanName);

        return invoke(target, methodName, paramMap);
    }

    /**
     * 根据参数名和参数个数匹配第一个方法
     */
    private Method findBestMethod(Class<?> clazz, String methodName, Map<String, Object> paramMap) {
        List<Method> candidates = Arrays.stream(clazz.getMethods()).filter(m -> StrUtil.equals(methodName, m.getName())).toList();
        if (CollUtil.isEmpty(candidates)) {
            throw new IllegalArgumentException("未找到方法: " + methodName);
        }

        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        for (Method method : candidates) {
            String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
            if (Objects.isNull(paramNames)) {
                if (CollUtil.isEmpty(paramMap)) {
                    return method;
                }
            } else {
                if (paramNames.length != CollUtil.size(paramMap)) {
                    continue;
                }
                if (Arrays.stream(paramNames).allMatch(paramMap::containsKey)) {
                    return method;
                }
            }
        }

        throw new IllegalArgumentException("未找到匹配参数的方法: " + methodName);
    }

    private Object[] buildArgs(Method method, Map<String, Object> paramMap) {
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        Type[] paramTypes = method.getGenericParameterTypes();
        Object[] args = new Object[paramTypes.length];
        if (ArrayUtil.isEmpty(paramNames)) {
            return args;
        }
        for (int i = 0; i < paramTypes.length; i++) {
            Object rawValue = paramMap.get(paramNames[i]);
            JavaType paramType = objectMapper.getTypeFactory().constructType(paramTypes[i]);
            args[i] = objectMapper.convertValue(rawValue, paramType);
        }
        return args;
    }
}
