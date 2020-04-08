package com.joezeo.joefgame.potal.service;

/**
 * @author Joezeo
 * @date 2020/4/8 13:20
 */
public interface IPService {
    /**
     * redis中记录该ip每分钟访问页面的次数
     * 如果超过时间限制 60/min 将该ip置入黑名单中
     * @param ip
     */
    void recordIPByRedis(String ip);

    /**
     * 在mysql中记录所有访问过网站的ip地址
     * @param ip
     */
    void recordIPByMysql(String ip);

    /**
     * 检查该ip是否存在于黑名单中
     * @param ip
     * @return
     */
    boolean isInBlackList(String ip);
}
