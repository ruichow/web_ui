package com.hand.api.exception;

public class APIResponseException extends APIException {

    private final int statusCode;

    public APIResponseException(Integer code, String reason) {
        super(reason);
        this.statusCode = code;
    }

    @Override
    public String toString() {
        return "APIResponseException [statusCode=" + statusCode + ", getMessage()=" + getMessage() + "]";
    }
}
