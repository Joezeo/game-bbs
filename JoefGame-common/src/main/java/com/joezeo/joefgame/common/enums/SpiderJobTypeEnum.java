package com.joezeo.joefgame.common.enums;

/**
 * 爬虫任务类型
 */
public enum SpiderJobTypeEnum {
    INIT_URL_DATA(1, "初始化 steam app url"),
    DAILY_CHECK_URL(2, "日常检查 steam app url 是否存在缺漏"),
    INIT_APP_INFO(3, "初始化 app 信息"),
    DAILY_CHECK_APP_INFO(4, "日常检查 app 信息是否有缺漏"),
    DAILY_SPIDE_SPECIAL_PRICE(5, "日常爬取steam特惠商品价格");

    private Integer type;
    private String name;

    SpiderJobTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
