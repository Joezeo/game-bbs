package com.joezeo.community.exception;

public enum CustomizeErrorCode implements IExceptionErrorCode{
    QUESTION_NOT_FOUND("您所搜索的问题不见了，请换一个试试");

    private String message;

    CustomizeErrorCode(String s) {
        this.message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
