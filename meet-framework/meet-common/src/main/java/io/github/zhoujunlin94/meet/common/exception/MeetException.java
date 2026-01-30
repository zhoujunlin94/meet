package io.github.zhoujunlin94.meet.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhoujl
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetException extends RuntimeException {

    private int code;
    private String message;
    private Object data;

    private MeetException(String msg) {
        this.code = CommonErrorCode.S_SYSTEM_BUSY.getCode();
        this.message = msg;
    }

    private MeetException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private MeetException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    private MeetException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getMessage(), throwable);
        this.code = errorCode.getCode();
        this.message = throwable.getMessage();
    }

    public static MeetException meet(ErrorCode errorCode) {
        return new MeetException(errorCode);
    }

    public static MeetException meet(ErrorCode errorCode, Object data) {
        return new MeetException(errorCode, data);
    }

    public static MeetException meet(ErrorCode errorCode, Throwable throwable) {
        return new MeetException(errorCode, throwable);
    }

    public static MeetException meet(String msg) {
        return new MeetException(msg);
    }

    public static MeetException meet(Object data) {
        return new MeetException(CommonErrorCode.S_SYSTEM_BUSY, data);
    }

}
