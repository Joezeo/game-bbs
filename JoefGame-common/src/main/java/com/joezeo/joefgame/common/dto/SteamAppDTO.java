package com.joezeo.joefgame.common.dto;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.List;

@Data
public class SteamAppDTO implements Serializable {
    private static final long serialVersionUID = 8537786774650172706L;

    // 以 @Field 注解标注的字段为在Solr中存储的字段
    @Field private String id;
    @Field private Integer appid;
    private Integer type;

    @Field private String name;
    @Field private String imgUrl;
    private String description;
    private String releaseDate;
    @Field private String devloper;
    private String publisher;
    @Field private Integer originalPrice;
    @Field private Integer finalPrice;
    private String summary;
    @Field private Integer appType;

    /*
    如果是sub礼包信息则需存储它所包含的app信息
     */
    private List<SteamAppDTO> includes;
}
