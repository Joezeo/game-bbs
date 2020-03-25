package com.joezeo.joefgame.common.dto;

import lombok.Data;

/**
 * 用于接收steam api：GetOwnedGames (v0001)的DTO类
 */
@Data
public class SteamGame {
    private Integer appid;

    private Integer playtime_forever;
    private Integer playtime_windows_forever;
    private Integer playtime_mac_forever;
    private Integer playtime_linux_forever;
}
