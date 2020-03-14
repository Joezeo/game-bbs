package com.joezeo.joefgame.potal.dto;

import com.joezeo.joefgame.dao.pojo.Topic;
import com.joezeo.joefgame.dao.pojo.User;
import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.List;

/**
 * Question 数据传输对象
 */

@Data
public class TopicDTO implements Serializable {
    private static final long serialVersionUID = -8402925568651851428L;

    // 以 @Field 注解标注的字段为在Solr中存储的字段
    private Long id;
    @Field private String topicid;
    @Field private String title;
    private String description;
    @Field private String tag;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    @Field private Long gmtCreate;
    private Long gmtModify;
    @Field private Long userid;
    private Integer topicType;

    private User user;
    private List<Topic> relateds;

    // 给这篇帖子点赞过的人的id集合
    private List<Long> likeUsersId;
}
