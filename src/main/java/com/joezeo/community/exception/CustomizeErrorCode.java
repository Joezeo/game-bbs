package com.joezeo.community.exception;

public enum CustomizeErrorCode implements IExceptionErrorCode{
    QUESTION_NOT_FOUND(2000,"该问题不见了，请换一个试试"),
    COMMENT_NOT_FOUND(2001, "该评论不见了，请换一个试试"),
    PARENT_ID_NOT_TRANSFER(2002, "请选定一个问题或评论进行回复！"),
    PARENT_TYPE_ILEEAGUE(2003, "非法的评论"),
    USER_NOT_LOGIN(2004, "当前操作需要登录后进行，请登录"),
    COMMENT_FAILD(2005, "评论失败，请稍后重试！"),
    ILLEGAL_TAG(2006, "存在非法标签！"),
    SERVER_ERROR(5001, "服务器出现异常了，请稍后重试"),
    READ_NOTIFICATION_FAILED(2007, "读取消息通知异常，请稍后重试"),
    NOTIFICATION_NOT_FOUND(2008, "消息通知不在了，请换一个试试"),
    QUESTION_ID_NOT_TRANSFER(2009, "该条评论所属问题不见了"),
    UPLOAD_IMG_FIILED(2010, "上传图片失败"),
    READ_ALL_FIALED(2011, "已读所有通知失败，请稍后重试");

    private Integer code;
    private String message;

    CustomizeErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
