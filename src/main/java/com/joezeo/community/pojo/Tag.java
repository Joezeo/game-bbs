package com.joezeo.community.pojo;

public class Tag {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tag.id
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tag.name
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tag.category
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    private Integer category;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_tag.id
     *
     * @return the value of t_tag.id
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_tag.id
     *
     * @param id the value for t_tag.id
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_tag.name
     *
     * @return the value of t_tag.name
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_tag.name
     *
     * @param name the value for t_tag.name
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_tag.category
     *
     * @return the value of t_tag.category
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_tag.category
     *
     * @param category the value for t_tag.category
     *
     * @mbg.generated Thu Jan 30 18:13:44 CST 2020
     */
    public void setCategory(Integer category) {
        this.category = category;
    }
}