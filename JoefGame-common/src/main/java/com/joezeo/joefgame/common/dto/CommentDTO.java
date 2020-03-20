package com.joezeo.joefgame.common.dto;

import com.joezeo.joefgame.dao.pojo.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -7912662336865259339L;

    /*
    需从前端接受的参数
     */
    private Long topicid;
    private Long parentId;
    private Integer parentType;
    private String content;

    private Long id;
    private Integer likeCount;
    private Integer commentCount;
    private Long gmtCreate;
    private Long gmtModify;
    private User user;
    private Long userid;

    /*
        与点赞相关的参数
     */
    private List<Long> likeUsersId; // 给该条评论点赞的用户id集合
    private Boolean likeStatus = false; // 当前用户给该条评论点赞的状态，false为还未点赞
    private String likeClass = ""; // 该条评论的class，如果点赞了为liked，否则为空
}
