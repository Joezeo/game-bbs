package com.joezeo.joefgame.potal.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1026452371438003405L;
    // 搜索时的搜索条件
    private String condition;

    // 以 @Field 注解标注的字段为在Solr中存储的字段
    private Long id;
    private String name;
    private String githubAccountId;
    private String token;
    private String bio;
    private String email;
    private String password;
    private String avatarUrl;
    private String authCode; // 用于邮箱验证的验证码
    private Boolean rememberMe; // 是否记住登录状态（7天）

    private List<String> roles;
}
