package com.joezeo.joefgame.dao.pojo;

public class TopicLikeUser {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_topic_like_user.id
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_topic_like_user.topicid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    private Long topicid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_topic_like_user.userid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    private Long userid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_topic_like_user.gmt_create
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_topic_like_user.gmt_modify
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    private Long gmtModify;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_topic_like_user.id
     *
     * @return the value of t_topic_like_user.id
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_topic_like_user.id
     *
     * @param id the value for t_topic_like_user.id
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_topic_like_user.topicid
     *
     * @return the value of t_topic_like_user.topicid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public Long getTopicid() {
        return topicid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_topic_like_user.topicid
     *
     * @param topicid the value for t_topic_like_user.topicid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public void setTopicid(Long topicid) {
        this.topicid = topicid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_topic_like_user.userid
     *
     * @return the value of t_topic_like_user.userid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_topic_like_user.userid
     *
     * @param userid the value for t_topic_like_user.userid
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_topic_like_user.gmt_create
     *
     * @return the value of t_topic_like_user.gmt_create
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_topic_like_user.gmt_create
     *
     * @param gmtCreate the value for t_topic_like_user.gmt_create
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_topic_like_user.gmt_modify
     *
     * @return the value of t_topic_like_user.gmt_modify
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public Long getGmtModify() {
        return gmtModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_topic_like_user.gmt_modify
     *
     * @param gmtModify the value for t_topic_like_user.gmt_modify
     *
     * @mbg.generated Tue Mar 17 17:33:38 CST 2020
     */
    public void setGmtModify(Long gmtModify) {
        this.gmtModify = gmtModify;
    }
}