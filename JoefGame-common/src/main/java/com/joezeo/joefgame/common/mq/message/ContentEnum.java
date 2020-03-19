package com.joezeo.joefgame.common.mq.message;

import com.joezeo.joefgame.dao.pojo.SteamAppInfo;

/**
 * 消息队列支持的可用作内容content的类型
 */
public enum  ContentEnum {
    STEAM_APP_INFO(SteamAppInfo.class)
    ;
    private Class<?> clazz;
    ContentEnum(Class<SteamAppInfo> steamAppInfo){
        this.clazz = steamAppInfo;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static String listType(){
        StringBuilder sb = new StringBuilder();
        for (ContentEnum value : ContentEnum.values()) {
            sb.append(value.getClazz()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static boolean hasType(Class<?> type){
        for (ContentEnum value : ContentEnum.values()) {
            if(type.equals(value.getClazz())){
                return true;
            }
        }
        return false;
    }
}
