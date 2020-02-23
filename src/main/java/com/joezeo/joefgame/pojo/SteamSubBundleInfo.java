package com.joezeo.joefgame.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SteamSubBundleInfo implements Serializable {

    private static final long serialVersionUID = 4514979868624056635L;

    private Integer id;
    private Integer appid;
    private String name;
    private String devloper;
    private String publisher;
    private String releaseDate;
    private Integer originalPrice;
    private Integer finalPrice;
    private String contains;
    private Long gmtCreate;
    private Long gmtModify;
    private String imgUrl;
    private String type;
}
