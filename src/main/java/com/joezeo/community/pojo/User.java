package com.joezeo.community.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable{
    private static final long serialVersionUID = 1069342836765321377L;

    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private String bio;
    private Long gmtCreate;
    private Long gmtModify;
    private String avatarUrl;
}
