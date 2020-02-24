package com.joezeo.joefgame.common.enums;

public enum  NotificationStatusEnum {
    UNREAD(0, "未读"),
    READED(1, "已读");
    ;
    private Integer status;
    private String name;

    NotificationStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
