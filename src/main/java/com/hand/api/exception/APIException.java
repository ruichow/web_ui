package com.hand.api.exception;

import com.hand.exception.BaseException;

public class APIException extends BaseException {

    public APIException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public APIException(String msg) {
        super(msg);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

}
