package io.github.zhoujunlin94.meet.web.aspect;

import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author zhoujunlin
 * @date 2023/03/28
 **/
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "endpoint.log.enable", havingValue = "true", matchIfMissing = true)
public class EndpointLogAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void endpointMethodPointcut() {
    }

    @Around("endpointMethodPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        logMethodInfo(pjp);
        return pjp.proceed();
    }

    private void logMethodInfo(ProceedingJoinPoint pjp) {
        try {
            String className = pjp.getSignature().getDeclaringType().getSimpleName();
            String methodName = pjp.getSignature().getName();
            log.warn("endpoint执行路径: {}#{}", className, methodName);

            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String method = request.getMethod();
            if (Method.POST.name().equalsIgnoreCase(method) || Method.PUT.toString().equalsIgnoreCase(method)) {
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Annotation[][] paramAnnotations = methodSignature.getMethod().getParameterAnnotations();
                Object[] args = pjp.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if ((arg instanceof MultipartFile)) {
                        continue;
                    }

                    for (Annotation annotation : paramAnnotations[i]) {
                        if (annotation instanceof RequestBody) {
                            log.warn("requestBody: {}", JSONObject.toJSONString(arg));
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("EndpointLogAspect logMethodInfo Meet Exception:", ex);
        }
    }

}
