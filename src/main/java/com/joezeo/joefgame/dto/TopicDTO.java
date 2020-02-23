package com.joezeo.joefgame.dto;

import com.joezeo.joefgame.pojo.Topic;
import com.joezeo.joefgame.pojo.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Question 数据传输对象
 */

@Data
public class TopicDTO implements Serializable {
    private static final long serialVersionUID = -8402925568651851428L;
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private Long gmtCreate;
    private Long gmtModify;
    private Long userid;
    private Integer topicType;

    private User user;
    private List<Topic> relateds;

    // 给这篇帖子点赞过的人的id集合
    private List<Long> likeUsersId;
}
