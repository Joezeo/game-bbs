package com.joezeo.community.exception;

public class CustomizeException extends RuntimeException {
    private static final long serialVersionUID = 7717821976286602129L;

    private Integer code;
    private String message;

    public CustomizeException(IExceptionErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
