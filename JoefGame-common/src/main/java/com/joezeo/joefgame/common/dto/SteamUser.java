package com.joezeo.joefgame.common.dto;

import lombok.Data;

@Data
public class SteamUser {
    private String personaname;
    private String steamid;

    /* 其余字段之后也许有用 */
    private String profileurl;
    private String realname;
    private Long timecreated;
    private String loccountrycode;
}
