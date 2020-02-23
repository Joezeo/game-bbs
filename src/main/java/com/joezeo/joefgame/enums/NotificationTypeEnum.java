package com.joezeo.joefgame.enums;

/**
 * 通知的类型
 */
public enum NotificationTypeEnum {
    QUESTION(1, "回复了帖子"),
    COMMENT(2, "回复了评论")
    ;
    private Integer type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String nameOf(Integer type){
        for (NotificationTypeEnum typeEnum : NotificationTypeEnum.values()) {
            if(type == typeEnum.getType()){
                return typeEnum.getName();
            }
        }

        return "";
    }
}
