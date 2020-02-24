package com.joezeo.joefgame.dao.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SteamUrl implements Serializable {

    private static final long serialVersionUID = -9090305859226525013L;

    private Integer id;
    private Integer appid;
    private String url;
}
