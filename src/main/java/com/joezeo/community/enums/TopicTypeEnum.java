package com.joezeo.community.enums;

public enum TopicTypeEnum {
    QUESTION(1, "question"),
    TECH(2, "tech"),
    CREATIVE(3, "creative"),
    PLAY(4, "play")
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
