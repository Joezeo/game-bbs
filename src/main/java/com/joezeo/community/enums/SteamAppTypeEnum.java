package com.joezeo.community.enums;

public enum SteamAppTypeEnum {
    GAME(1, "game"),
    SOFTWARE(2, "software"),
    DLC(3, "dlc"),
    DEMO(4, "demo"),
    BUNDLE(5, "bundle"),
    SOUND(6, "sound");

    private Integer index;
    private String type;

    SteamAppTypeEnum(Integer index, String type) {
        this.index = index;
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public static String typeOf(Integer index) {
        for (SteamAppTypeEnum typeEnum : SteamAppTypeEnum.values()) {
            if (typeEnum.getIndex() == index) {
                return typeEnum.getType();
            }
        }
        return null;
    }
}
