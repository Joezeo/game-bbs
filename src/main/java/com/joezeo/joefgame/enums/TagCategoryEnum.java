package com.joezeo.joefgame.enums;

public enum TagCategoryEnum {
    LANGUAGE(1, "开发语言"),
    FRAMEWORK(2, "平台框架"),
    SERVER(3, "服务器"),
    DATABASE(4, "数据库"),
    TOOL(5, "开发工具")
    ;

    private Integer index;
    private String category;
    TagCategoryEnum(Integer index, String category) {
        this.index = index;
        this.category = category;
    }

    public Integer getIndex() {
        return index;
    }

    public String getCategory() {
        return category;
    }
}
