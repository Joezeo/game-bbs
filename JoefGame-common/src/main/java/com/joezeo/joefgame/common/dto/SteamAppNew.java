package com.joezeo.joefgame.common.dto;

import lombok.Data;

@Data
public class SteamAppNew {
    private Long gid;
    private String title;
    private String url;
    private boolean is_external_url;
    private String author;
    private String contents;
    private Long date;

    public void setDate(Long date){
        this.date = date*1000; // steam uri 的date是UnixTimeStamp，以秒为单位
    }
}
