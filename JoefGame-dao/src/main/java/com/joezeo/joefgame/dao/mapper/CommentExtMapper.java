package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.Comment;

public interface CommentExtMapper {
    /**
     * 累加二级评论数
     */
    int incComment(Comment comment);

    int incLike(Comment comment);

    int decLike(Comment comment);
}