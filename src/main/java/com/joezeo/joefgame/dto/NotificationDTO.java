package com.joezeo.joefgame.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier; // 消息通知的发起者id
    private String notifiername; // 消息接收者姓名
    private Long relatedid; // 回复帖子/评论的id
    private String type; // 回复类型 （帖子or评论）
    private String relatedname; // 所回复的对象的内容
    private Integer status; // 已读 or 未读
    private Long gmt_create; // 通知创建时间
}
