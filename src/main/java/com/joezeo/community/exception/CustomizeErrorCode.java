package com.joezeo.community.exception;

public enum CustomizeErrorCode implements IExceptionErrorCode{
    SERVER_ERROR(5001, "系统出错了，请稍后重试"),
    QUESTION_NOT_FOUND(2000,"该帖子不见了，请换一个试试"),
    COMMENT_NOT_FOUND(2001, "该评论不见了，请换一个试试"),
    PARENT_ID_NOT_TRANSFER(2002, "请选定一个帖子或评论进行回复！"),
    PARENT_TYPE_ILEEAGUE(2003, "非法的评论"),
    USER_NOT_LOGIN(2004, "当前操作需要登录后进行，请登录"),
    COMMENT_FAILD(2005, "评论失败，请稍后重试！"),
    ILLEGAL_TAG(2006, "存在非法标签！"),
    READ_NOTIFICATION_FAILED(2007, "读取消息通知异常，请稍后重试"),
    NOTIFICATION_NOT_FOUND(2008, "消息通知不在了，请换一个试试"),
    QUESTION_ID_NOT_TRANSFER(2009, "该条评论所属帖子不见了"),
    UPLOAD_IMG_FIILED(2010, "上传图片失败"),
    READ_ALL_FIALED(2011, "已读所有通知失败，请稍后重试"),
    DOWNLOAD_TOPIC_FAILED(2012, "获取帖子内容失败，请稍后重试"),
    TOPIC_NOT_FOUND(2013, "你查找的帖子不存在或已被删除，请换一个试试"),
    SPIDE_STEAM_URL_ERROR(2014, "爬取steam应用url失败"),
    CREATE_NEW_USER_FAILD(2015, "注册新用户失败啦，请稍后重试~"),
    UPDATE_USER_FAILD(2016, "更新用户信息失败啦。请稍后重试~"),
    INIT_PRICE_CHART_FAILED(2017, "Opps，初始化价格图表失败了，请稍后重试");

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
