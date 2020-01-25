package com.joezeo.community.dto;

import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Question 数据传输对象
 */

@Data
public class QuestionDTO implements Serializable {
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

    private User user;
    private List<Question> relateds;
}
