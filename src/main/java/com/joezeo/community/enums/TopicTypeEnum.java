package com.joezeo.community.enums;

public enum TopicTypeEnum {
    NEWS(1, "news"),
    ESSAY(2, "essay"),
    SQUARE(3, "square")
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
