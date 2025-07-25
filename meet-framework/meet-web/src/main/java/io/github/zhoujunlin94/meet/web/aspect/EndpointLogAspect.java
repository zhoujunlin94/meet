package io.github.zhoujunlin94.meet.web.aspect;

import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

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
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

            String className = pjp.getSignature().getDeclaringType().getSimpleName();
            String methodName = pjp.getSignature().getName();
            log.warn("接口执行路径: {}#{}", className, methodName);

            String method = request.getMethod();
            if (Method.POST.toString().equalsIgnoreCase(method) || Method.PUT.toString().equalsIgnoreCase(method)) {
                Object[] args = pjp.getArgs();
                for (Object arg : args) {
                    if (!(arg instanceof MultipartFile)) {
                        log.warn("RequestBody: {}", JSONObject.toJSONString(arg));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("EndpointLogAspect logMethodInfo Meet Exception:", ex);
        }
    }

}
