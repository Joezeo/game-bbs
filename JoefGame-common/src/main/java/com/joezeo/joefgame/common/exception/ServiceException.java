package com.joezeo.joefgame.common.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -1210761942419862543L;

    public ServiceException(String message) {
        super(message);
    }
}
