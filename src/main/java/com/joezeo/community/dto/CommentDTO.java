package com.joezeo.community.dto;

import com.joezeo.community.pojo.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -7912662336865259339L;

    /*
    需从前端接受的参数
     */
    private Long parentId;
    private Integer parentType;
    private String content;

    private Long id;
    private Integer likeCount;
    private Integer commentCount;
    private Long gmtCreate;
    private Long gmtModify;
    private User user;
}
