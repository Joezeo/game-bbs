package com.joezeo.joefgame.potal.dto;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1026452371438003405L;

    // 以 @Field 注解标注的字段为在Solr中存储的字段
    private Long id;
    @Field private String userid;
    @Field private String name;
    private String githubAccountId;
    @Field private String bio;
    private String email;
    private String password;
    @Field private String avatarUrl;
    private String authCode; // 用于邮箱验证的验证码
    private Boolean rememberMe; // 是否记住登录状态（7天）
    private String steamId;

    private List<String> roles;
}
