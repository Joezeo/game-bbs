package com.joezeo.joefgame.common.enums;

public enum  CommentTypeEnum {
    QUESTION(0),
    COMMENT(1)
    ;

    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(int type) {
        this.type = type;
    }

    public static boolean isExist(Integer type){
        for(CommentTypeEnum v : CommentTypeEnum.values()){
            if(type == v.getType()){
                return true;
            }
        }
        return false;
    }
}
