package com.joezeo.joefgame.common.dto;

import lombok.Data;

/**
 * 用户动态信息：
 * {
 *     用户发帖信息,
 *     TODO: 用户动态
 * }
 */
@Data
public class UserPostDTO {
    /*
        存储帖子、动态公共内容
     */
    private Long posterId;
    private String posterName;
    private String postTitle;
    private Long postTime;
    private String avatarUrl;
    private Integer type;

    /*
        动态相关属性
        TODO: 需要完善
     */
    private String postContent;
}
