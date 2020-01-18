package com.joezeo.community.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Question implements Serializable {
    private static final long serialVersionUID = 5201358938874416235L;

    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private Long gmtCreate;
    private Long gmtModify;
    private Integer userid;

}
