package com.joezeo.joefgame.common.enums;

import com.joezeo.joefgame.common.exception.IExceptionErrorCode;

public enum CustomizeErrorCode implements IExceptionErrorCode {
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
    INIT_PRICE_CHART_FAILED(2017, "Opps，初始化价格图表失败了，请稍后重试"),
    LIKE_TOPIC_FAILED(2018, "ops，点赞失败了，请过会儿试试"),
    UNLIKE_TOPIC_FAILED(2019, "ops，取消点赞失败了，请过会儿试试"),
    LIKE_COMMENT_FAILED(2020, "ops,点赞评论失败了，请过会儿试试"),
    UNLIKE_COMMENT_FAILED(2021, "ops,取消点赞失败了，请过会儿试试"),
    USER_LOGIN_FALIED(2022, "用户名或密码错误，请检查后重试"),
    USER_LOGIN_FALIED_UNKONWN(2023, "登录发生未知错误，请稍后重试"),
    ONLY_ACCEPT_BPMN(2024, "只支持.bpmn格式的流程定义文件上传，请重试"),
    DEPLOY_PROCESS_ERROR(2025, "部署流程文件错误，请稍后重试"),
    DELETE_PROCESS_ERROR(2026, "删除流程定义失败，请稍后重试"),
    GET_PROCESS_PIC_FALIED(2027, "获取流程定义图片失败，请稍后重试"),
    APP_NOT_FOUND(2028, "您查询的App不存在，请换一个试试"),
    EMAIL_HAS_EXISTED(2029, "该邮箱已被注册使用，请更换后使用或直接登录"),
    USER_GITHUB_LOGIN_FALIED(2030, "使用Github三方登录失败，请稍后重试"),
    SIGNUP_WRONG_AUTHCODE(2031, "验证码错误，请检查后重试，或重新获取验证码"),
    AUTHCODE_TIME_OUT(2032, "验证码未获取或验证码已超时，请重新获取"),
    SEARCH_FAILED(2033, "发生未知原因搜索失败，请换个搜索条件试试"),
    USER_STEAM_LOGIN_FALIED(2034, "使用Steam三方登录失败，请稍后重试"),
    FAVORITE_APP_FAILED(2035, "收藏应用失败，请稍后重试"),
    UNFAVORITE_APP_FAILED(2036, "取消收藏应用失败，请稍后重试"),
    STEAM_AUTH_FAILD(2037, "Steam三方认证失败"),
    STEAM_CONNECTION_FAILD(2038, "连接Steam服务失败"),
    FETCH_POSTS_FAILD(2039, "获取动态信息失败"),
    UPLOAD_TOPIC_FIILED(2040, "上传帖子内容至UCloud失败"),
    UPLOAD_AVATAR_FAILED(2041, "上传新头像失败"),
    GENERATE_RANDOM_AVATAR_FAILED(2042, "生成随机头像失败"),
    UPDATE_BIO_FAILED(2043, "修改个性签名失败");

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
