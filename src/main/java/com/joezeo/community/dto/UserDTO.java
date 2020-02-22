package com.joezeo.community.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1026452371438003405L;
    private Long id;
    private String name;
    private String githubAccountId;
    private String token;
    private String bio;
    private String email;
    private String password;
    private Integer authCode; // 用于邮箱验证的验证码
    private Boolean rememberMe; // 是否记住登录状态（7天）
}
