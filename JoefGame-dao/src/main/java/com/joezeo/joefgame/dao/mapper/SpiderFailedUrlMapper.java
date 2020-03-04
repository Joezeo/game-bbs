package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.SpiderFailedUrl;
import com.joezeo.joefgame.dao.pojo.SpiderFailedUrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SpiderFailedUrlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    long countByExample(SpiderFailedUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int deleteByExample(SpiderFailedUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int insert(SpiderFailedUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int insertSelective(SpiderFailedUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    List<SpiderFailedUrl> selectByExampleWithRowbounds(SpiderFailedUrlExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    List<SpiderFailedUrl> selectByExample(SpiderFailedUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    SpiderFailedUrl selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int updateByExampleSelective(@Param("record") SpiderFailedUrl record, @Param("example") SpiderFailedUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int updateByExample(@Param("record") SpiderFailedUrl record, @Param("example") SpiderFailedUrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int updateByPrimaryKeySelective(SpiderFailedUrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_spider_failed_url
     *
     * @mbg.generated Wed Mar 04 16:43:25 CST 2020
     */
    int updateByPrimaryKey(SpiderFailedUrl record);
}