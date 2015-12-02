package com.mogujie.service.redis.msg.exception;

/**
 * Created by zhizhu on 15/11/19.
 */
public class RedisMsgException extends AbstractRedisMsgException {
    private Integer code;
    private String errorMessage;

    public RedisMsgException() {
        super();
    }

    public RedisMsgException(String message) {
        super(message);
    }

    public RedisMsgException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisMsgException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
