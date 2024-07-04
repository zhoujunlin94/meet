package io.github.zhoujunlin94.meet.rocketmq.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.common.util.RequestIdUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author zhoujunlin
 * @date 2024/7/3 21:31
 */
@Slf4j
@AllArgsConstructor
public class RocketMQHelper {

    private RocketMQTemplate rocketMQTemplate;

    public boolean syncSend(String topic, Object payload) {
        log.warn("syncSend RocketMQ, topic:{}, payload:{}", topic, payload);
        SendResult sendResult = rocketMQTemplate.syncSend(topic, payload);
        log.warn("syncSend RocketMQ SendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public void asyncSend(String topic, Object payload, BiConsumer<String, Object> successConsumer, BiConsumer<String, Object> exceptionConsumer) {
        log.warn("asyncSend RocketMQ, topic:{}, payload:{}", topic, payload);
        String requestId = MDC.get(RequestIdUtil.REQUEST_ID);
        rocketMQTemplate.asyncSend(topic, payload, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.warn("[asyncSend][消息体:{} 发送成功, 结果为:{}]", payload, sendResult);
                successConsumer.accept(topic, payload);
            }

            @Override
            public void onException(Throwable throwable) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.warn("[asyncSend][消息体:{} 发送异常]", payload, throwable);
                exceptionConsumer.accept(topic, payload);
            }

        });
    }

    public void sendOneWay(String topic, Object payload) {
        log.warn("sendOneWay RocketMQ, topic:{}, payload:{}", topic, payload);
        rocketMQTemplate.sendOneWay(topic, payload);
    }

    public boolean syncSendBatch(String topic, List<Message> messages, Integer timeout, TimeUnit timeUnit) {
        log.warn("syncSendBatchMessage RocketMQ, topic:{}, messages:{}", topic, JSONObject.toJSONString(messages));
        if (CollUtil.isEmpty(messages)) {
            return false;
        }
        SendResult sendResult = rocketMQTemplate.syncSend(topic, messages, timeUnit.toMillis(timeout));
        log.warn("syncSendBatchMessage RocketMQ sendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public boolean syncSendBatch(String topic, List<Object> payloads) {
        if (CollUtil.isEmpty(payloads)) {
            return false;
        }
        List<Message> messages = payloads.stream().map(payload -> MessageBuilder.withPayload(payload).build()).collect(Collectors.toList());
        return syncSendBatch(topic, messages, 10, TimeUnit.SECONDS);
    }

    public boolean syncDelaySend(String topic, Message message, Integer delayLevel, Integer timeout, TimeUnit timeUnit) {
        log.warn("syncDelaySend RocketMQ, topic:{}, message:{}", topic, JSONObject.toJSONString(message));
        SendResult sendResult = rocketMQTemplate.syncSend(topic, message, timeUnit.toMillis(timeout), delayLevel);
        log.warn("syncDelaySend RocketMQ, sendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public boolean syncDelaySend(String topic, Object payload, Integer delayLevel) {
        Message<Object> message = MessageBuilder.withPayload(payload).build();
        return syncDelaySend(topic, message, delayLevel, 10, TimeUnit.SECONDS);
    }

    public void asyncDelaySend(String topic, Message message, Integer delayLevel, Integer timeout, TimeUnit timeUnit, BiConsumer<String, Object> successConsumer, BiConsumer<String, Object> exceptionConsumer) {
        log.warn("asyncDelaySend RocketMQ, topic:{}, message:{}", topic, JSONObject.toJSONString(message));
        String requestId = MDC.get(RequestIdUtil.REQUEST_ID);
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.info("[asyncDelaySend][发送成功,结果为:{}]", sendResult);
                successConsumer.accept(topic, message);
            }

            @Override
            public void onException(Throwable throwable) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.info("[asyncDelaySend][发送异常]", throwable);
                exceptionConsumer.accept(topic, message);
            }
        }, timeUnit.toMillis(timeout), delayLevel);
    }

    public void asyncDelaySend(String topic, Object payload, Integer delayLevel, BiConsumer<String, Object> successConsumer, BiConsumer<String, Object> exceptionConsumer) {
        Message<Object> message = MessageBuilder.withPayload(payload).build();
        asyncDelaySend(topic, message, delayLevel, 10, TimeUnit.SECONDS, successConsumer, exceptionConsumer);
    }

    public boolean syncSendOrderly(String topic, Object payload, String hashKey) {
        log.warn("syncSendOrderly RocketMQ, topic:{}, payload:{}, hashKey:{} ", topic, payload, hashKey);
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(topic, payload, hashKey);
        log.warn("syncSendOrderly RocketMQ, sendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public void asyncSendOrderly(String topic, Object payload, String hashKey, BiConsumer<String, Object> successConsumer, BiConsumer<String, Object> exceptionConsumer) {
        log.warn("asyncSendOrderly RocketMQ, topic:{}, payload:{}, hashKey:{} ", topic, payload, hashKey);
        String requestId = MDC.get(RequestIdUtil.REQUEST_ID);
        rocketMQTemplate.asyncSendOrderly(topic, payload, hashKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.info("[asyncSendOrderly][发送成功，结果为：[{}]]", result);
                successConsumer.accept(topic, payload);
            }

            @Override
            public void onException(Throwable e) {
                MDC.put(RequestIdUtil.REQUEST_ID, requestId);
                log.info("[asyncSendOrderly][发送异常]]", e);
                exceptionConsumer.accept(topic, payload);
            }
        });
    }

    public void sendOneWayOrderly(String topic, Object payload, String hashKey) {
        log.warn("sendOneWayOrderly RocketMQ, topic:{}, payload:{}, hashKey:{} ", topic, payload, hashKey);
        rocketMQTemplate.sendOneWayOrderly(topic, payload, hashKey);
    }

    public boolean syncSendInTag(String topic, String tag, Object payload) {
        log.warn("sendWithTag RocketMQ, topic:{}, tag:{}, payload:{}", topic, tag, payload);
        SendResult sendResult = rocketMQTemplate.syncSend(makeDestination(topic, tag), payload);
        log.warn("sendWithTag RocketMQ, sendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }


    public boolean syncSendInSql(String topic, Object payload, Map<String, Object> sqlMap) {
        log.warn("syncSendWithSql RocketMQ, topic:{}, payload:{}, sql:{}", topic, payload, sqlMap);
        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(payload);
        if (CollUtil.isNotEmpty(sqlMap)) {
            sqlMap.forEach(messageBuilder::setHeader);
        }
        SendResult sendResult = rocketMQTemplate.syncSend(topic, messageBuilder.build());
        log.warn("syncSendWithSql RocketMQ, sendResult:{}", sendResult);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public boolean sendMessageInTransaction(String topic, String tag, Message message, Object extArg) {
        log.warn("sendMessageInTransaction RocketMQ, topic:{}, tag:{}, message:{}, extArg:{}", topic, tag, JSONObject.toJSONString(message), extArg);
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(makeDestination(topic, tag), message, extArg);
        log.warn("sendMessageInTransaction RocketMQ, transactionSendResult:{}", transactionSendResult);
        return transactionSendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    public boolean sendMessageInTransaction(String topic, String tag, Object payload) {
        log.warn("sendMessageInTransaction RocketMQ, topic:{}, tag:{}, payload:{}", topic, tag, payload);
        String destination = makeDestination(topic, tag);
        Message<Object> message = MessageBuilder.withPayload(payload).build();
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
        log.warn("sendMessageInTransaction RocketMQ, transactionSendResult:{}", transactionSendResult);
        return transactionSendResult.getSendStatus() == SendStatus.SEND_OK;
    }

    private String makeDestination(String topic, String tag) {
        return StrUtil.join(StrUtil.COLON, topic, tag);
    }

}
