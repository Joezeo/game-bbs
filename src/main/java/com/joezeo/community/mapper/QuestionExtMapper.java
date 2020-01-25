package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExtMapper {
    /**
     * 用于累加阅读数
     */
    int incView(Question record);

    /**
     * 累加评论数
     */
    int incComment(Question record);

    /**
     * 根据正则表达式获取相关问题
     */
    List<Question> selectRelated(@Param("curId") Long curId, @Param("tagRegex") String tagRegex);
}