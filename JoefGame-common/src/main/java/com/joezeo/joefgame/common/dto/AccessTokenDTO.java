package com.joezeo.joefgame.common.dto;

import lombok.Data;

/**
 * 用于获取Github access_token 时所需的参数数据传输对象
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
