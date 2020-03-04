package com.joezeo.joefgame.potal.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    private String avatarUrl;
    private Integer authCode; // 用于邮箱验证的验证码
    private Boolean rememberMe; // 是否记住登录状态（7天）

    private List<String> roles;
}
