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

    /**
     * 用于主页展示问题或者搜索问题
     * 查询总数量
     * 使用正则表达式匹配搜索条件
     */
    int countSearch(String condition);

    /**
     * 用于主页展示问题或者搜索问题的分页查询
     * 使用正则表达式匹配搜索条件
     */
    List<Question> selectSearch(@Param("index") int index, @Param("size") Integer size, @Param("condition") String condition);
}