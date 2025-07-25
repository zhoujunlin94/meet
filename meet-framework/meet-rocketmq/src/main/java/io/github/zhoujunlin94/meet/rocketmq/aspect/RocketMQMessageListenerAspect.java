package io.github.zhoujunlin94.meet.rocketmq.aspect;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;

/**
 * @author zhoujunlin
 * @date 2024/7/3 22:17
 */
@Slf4j
@Aspect
public class RocketMQMessageListenerAspect {

    @Pointcut("@within(org.apache.rocketmq.spring.annotation.RocketMQMessageListener) && execution(* onMessage(..))")
    public void onMessage() {
    }

    @Around("onMessage()")
    public Object aroundOnMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put(RequestIdUtil.REQUEST_ID, RequestIdUtil.generateRequestId());
        StopWatch stopWatch = new StopWatch();
        try {
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            log.warn("RocketMQ监听对象{}#{}开始消费", className, methodName);
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                log.warn("消息体:{}", JSONObject.toJSONString(args[0]));
            }
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
                log.warn("消费结束,耗时:{}ms", stopWatch.getTotalTimeMillis());
            }
            MDC.remove(RequestIdUtil.REQUEST_ID);
        }
    }

}
