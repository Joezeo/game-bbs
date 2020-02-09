package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Topic;
import com.joezeo.community.pojo.TopicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TopicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    long countByExample(TopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int deleteByExample(TopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int insert(Topic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int insertSelective(Topic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    List<Topic> selectByExampleWithRowbounds(TopicExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    List<Topic> selectByExample(TopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    Topic selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int updateByExampleSelective(@Param("record") Topic record, @Param("example") TopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int updateByExample(@Param("record") Topic record, @Param("example") TopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int updateByPrimaryKeySelective(Topic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_topic
     *
     * @mbg.generated Sun Feb 09 17:36:21 CST 2020
     */
    int updateByPrimaryKey(Topic record);
}