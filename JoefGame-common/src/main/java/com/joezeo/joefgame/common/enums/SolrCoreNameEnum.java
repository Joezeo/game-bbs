package com.joezeo.joefgame.common.enums;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SolrCoreNameEnum {
    STEAMAPP_GAME("steamapp_game", 1, "game"),
    STEAMAPP_SOFTWARE("steamapp_software", 2, "software"),
    STEAMAPP_DLC("steamapp_dlc", 3, "dlc"),
    STEAMAPP_DEMOGAME("steamapp_demogame", 4, "demo"),
    STEAMAPP_SUBBUNDLE("steamapp_subbundle", 5, "sub/bundle"),
    STEAMAPP_SOUNDTAPE("steamapp_soundtape", 6, "sound"),
    USER("user", 8, null),
    TOPIC("topic", 9, null);
    private String name;
    private Integer index;
    private String appType;

    SolrCoreNameEnum(String name, Integer index, String appType) {
        this.name = name;
        this.index = index;
        this.appType = appType;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public String getAppType() {
        return appType;
    }

    public static List<String> listSteamCores() {
        SolrCoreNameEnum[] values = SolrCoreNameEnum.values();
        List<String> cores = new ArrayList<>();
        Arrays.stream(values).forEach(core -> {
            String name = core.getName();
            if (name.contains("steamapp")) {
                cores.add(name);
            }
        });
        return cores;
    }

    public static String nameOf(@NotNull Integer index) {
        if (index == 7 || index == 5) {
            return SolrCoreNameEnum.STEAMAPP_SUBBUNDLE.getName();
        }
        for (SolrCoreNameEnum value : SolrCoreNameEnum.values()) {
            if (index.equals(value.getIndex())) {
                return value.getName();
            }
        }

        return null;
    }

    public static String nameOf(@NotNull String type) {
        if ("sub".equals(type) || "bundle".equals(type)) {
            return SolrCoreNameEnum.STEAMAPP_SUBBUNDLE.getName();
        }
        for (SolrCoreNameEnum value : SolrCoreNameEnum.values()) {
            if (type.equals(value.getAppType())) {
                return value.getName();
            }
        }

        return null;
    }
}
