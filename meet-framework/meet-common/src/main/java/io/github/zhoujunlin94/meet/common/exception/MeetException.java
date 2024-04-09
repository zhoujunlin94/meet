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
    private String msg;
    private Object data;

    private MeetException(String msg) {
        this.code = CommonErrorCode.S_SYSTEM_BUSY.getCode();
        this.msg = msg;
    }

    private MeetException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    private MeetException(ErrorCode errorCode, Object data) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.data = data;
    }

    private MeetException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getMsg(), throwable);
        this.code = errorCode.getCode();
        this.msg = throwable.getMessage();
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
