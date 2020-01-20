package com.joezeo.community.exception;

public class CustomizeException extends RuntimeException {
    private static final long serialVersionUID = 7717821976286602129L;

    public CustomizeException(IExceptionErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
