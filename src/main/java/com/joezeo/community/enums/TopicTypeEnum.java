package com.joezeo.community.enums;

public enum TopicTypeEnum {
    SALES(1, "sales"),
    NEWS(2, "news"),
    GUIDE(3, "guide"),
    ESSAY(4, "essay"),
    SQUARE(5, "square")
    ;
    private Integer type;
    private String name;

    TopicTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static Integer typeOfName(String name){
        for (TopicTypeEnum value : TopicTypeEnum.values()) {
            if(value.name.equals(name)){
                return value.type;
            }
        }
        return 0;
    }
}
