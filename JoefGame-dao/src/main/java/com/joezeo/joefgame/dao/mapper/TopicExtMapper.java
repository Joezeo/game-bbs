package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicExtMapper {
    /**
     * 用于累加阅读数
     */
    int incView(Topic record);

    /**
     * 累加评论数
     */
    int incComment(Topic record);

    /**
     * 累加点赞数
     */
    int incLike(Topic record);

    /**
     * 减少点赞数
     */
    int decLike(Topic topic);

    /**
     * 根据正则表达式获取相关帖子
     */
    List<Topic> selectRelated(@Param("curId") Long curId, @Param("tagRegex") String tagRegex);

    /**
     * 用于主页展示帖子或者搜索帖子
     * 查询总数量
     * 使用正则表达式匹配搜索条件
     */
    int countSearch(@Param("condition") String condition, @Param("type") Integer type);

    /**
     * 用于主页展示帖子或者搜索帖子的分页查询
     * 使用正则表达式匹配搜索条件
     */
    List<Topic> selectSearch(@Param("index") int index,
                             @Param("size") Integer size,
                             @Param("condition") String condition,
                             @Param("type") Integer type);

}