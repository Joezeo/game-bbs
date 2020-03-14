package com.joezeo.joefgame.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum  SolrCoreNameEnum {
    STEAMAPP_GAME("steamapp_game"),
    STEAMAPP_DLC("steamapp_dlc"),
    STEAMAPP_SOFTWARE("steamapp_software"),
    STEAMAPP_DEMOGAME("steamapp_demogame"),
    STEAMAPP_SOUNDTAPE("steamapp_soundtape"),
    STEAMAPP_SUBBUNDLE("steamapp_subbundle"),
    USER("user"),
    TOPIC("topic")
    ;
    private String name;
    SolrCoreNameEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> listSteamCores(){
        SolrCoreNameEnum[] values = SolrCoreNameEnum.values();
        List<String> cores = new ArrayList<>();
        Arrays.stream(values).forEach(core -> {
            String name = core.getName();
            if(name.contains("steamapp")){
                cores.add(name);
            }
        });
        return cores;
    }
}
