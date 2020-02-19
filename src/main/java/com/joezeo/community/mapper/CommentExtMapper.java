package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Comment;
import com.joezeo.community.pojo.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    /**
     * 累加二级评论数
     */
    int incComment(Comment comment);

    int incLike(Comment comment);

    int decLike(Comment comment);
}