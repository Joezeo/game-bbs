package com.joezeo.joefgame.dao.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SteamAppInfo implements Serializable {
    private static final long serialVersionUID = 2399622983552432428L;

    private Integer id;
    private Integer appid;
    private String name;
    private String imgUrl;
    private String description;
    private String releaseDate;
    private String devloper;
    private String publisher;
    private Integer originalPrice;
    private Integer finalPrice;
    private String summary;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer appType;
}
