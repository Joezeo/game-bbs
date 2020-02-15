package com.joezeo.community.dto;

import lombok.Data;

import java.io.Serializable;

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
}
