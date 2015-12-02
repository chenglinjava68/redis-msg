package com.mogujie.service.redis.msg.exception;

/**
 * Created by zhizhu on 15/11/19.
 */
public abstract class AbstractRedisMsgException extends RuntimeException {


    public AbstractRedisMsgException() {
        super();
    }

    public AbstractRedisMsgException(String message) {
        super(message);
    }

    public AbstractRedisMsgException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractRedisMsgException(Throwable cause) {
        super(cause);
    }

}
