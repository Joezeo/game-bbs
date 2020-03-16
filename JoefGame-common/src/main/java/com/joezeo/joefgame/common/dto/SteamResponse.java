package com.joezeo.joefgame.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class SteamResponse {
    private SteamResponse response;
    private List<SteamUser> players;
}
