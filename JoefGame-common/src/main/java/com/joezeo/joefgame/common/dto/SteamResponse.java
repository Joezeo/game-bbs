package com.joezeo.joefgame.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class SteamResponse {
    private SteamResponse response;

    /*api：GetPlayerSummaries (v0002) 相关属性*/
    private List<SteamUser> players;

    /*api：GetOwnedGames (v0001) 相关属性*/
    private Integer game_count;
    private List<SteamGame> games;

    /*api：GetNewsForApp (v0002) 相关属性*/
    private SteamResponse appnews;
    private List<SteamAppNew> newsitems;

    /*api：GetNumberOfCurrentPlayers (v0001) 相关属性*/
    private Integer player_count;
}
