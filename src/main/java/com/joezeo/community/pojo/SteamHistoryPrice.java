package com.joezeo.community.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SteamHistoryPrice implements Serializable {
    private static final long serialVersionUID = -5756448848073019088L;

    private Long id;
    private Integer appid;
    private Integer price;
    private Long gmtCreate;
}
