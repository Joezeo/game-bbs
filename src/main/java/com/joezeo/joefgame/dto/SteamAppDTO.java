package com.joezeo.joefgame.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SteamAppDTO implements Serializable {
    private static final long serialVersionUID = 8537786774650172706L;

    private Integer appid;
    private Integer type;

    private String name;
    private String imgUrl;
    private String description;
    private String releaseDate;
    private String devloper;
    private String publisher;
    private Integer originalPrice;
    private Integer finalPrice;
    private String summary;

    /*
    如果是sub礼包信息则需存储它所包含的app信息
     */
    private List<SteamAppDTO> includes;
}
