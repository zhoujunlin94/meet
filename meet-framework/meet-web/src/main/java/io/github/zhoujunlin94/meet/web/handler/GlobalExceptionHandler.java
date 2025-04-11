package io.github.zhoujunlin94.meet.web.handler;

import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.exception.CommonErrorCode;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import io.github.zhoujunlin94.meet.common.pojo.JsonResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;
import java.util.Optional;

/**
 * @Author zhoujl
 * @Date 2020/5/5 14:34
 * @Description
 **/
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE - 1)
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JsonResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Iterator<ObjectError> errorIterator = e.getBindingResult().getAllErrors().iterator();
        JsonResponse jsonResponse = JsonResponse.builder().code(CommonErrorCode.P_BAD_PARAMETER.getCode()).msg(e.getMessage()).build();
        if (errorIterator.hasNext()) {
            ObjectError error = errorIterator.next();
            jsonResponse.setMsg(error.getDefaultMessage());
        }
        return jsonResponse;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public JsonResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return JsonResponse.builder().code(CommonErrorCode.P_BAD_PARAMETER.getCode()).msg(e.getMessage()).build();
    }

    @ExceptionHandler({MeetException.class})
    public JsonResponse handleMeetException(MeetException e) {
        return JsonResponse.builder().code(e.getCode()).data(e.getData()).msg(e.getMsg()).build();
    }

    @ExceptionHandler({Exception.class})
    public JsonResponse handleUnknownException(Exception e) {
        log.error("未知异常:", e);
        if (e instanceof UndeclaredThrowableException) {
            Throwable undeclaredThrowable = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
            // todo 后续有一些可预期的异常可以instanceof判断生成相应响应
        }
        return JsonResponse.builder().code(CommonErrorCode.S_SYSTEM_BUSY.getCode()).msg(e.getMessage()).build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public JsonResponse handleConstraintViolationException(ConstraintViolationException e) {
        Optional<ConstraintViolation<?>> constraintViolationOpt = e.getConstraintViolations().stream().findFirst();
        String messageTemplate = StrUtil.EMPTY;
        if (constraintViolationOpt.isPresent()) {
            messageTemplate = constraintViolationOpt.get().getMessageTemplate();
        }
        return JsonResponse.builder().code(CommonErrorCode.P_PARAM_CHECK_ERROR.getCode()).msg("参数校验未通过!").data(messageTemplate).build();
    }

}
