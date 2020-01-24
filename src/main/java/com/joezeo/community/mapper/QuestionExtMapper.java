package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Question;

public interface QuestionExtMapper {
    /**
     * 用于累加阅读数
     */
    int incView(Question record);

    /**
     * 累加评论数
     */
    int incComment(Question record);
}