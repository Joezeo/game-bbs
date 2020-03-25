package com.joezeo.joefgame.common.enums;

/**
 * Home页面 '关注动态'展示的动态类型
 */
public enum PostTypeEnum {
    TOPIC(1, "topic"),
    POST(2, "post")
    ;
    private Integer index;
    private String type;

    PostTypeEnum(Integer index, String type){
        this.index = index;
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }
}
