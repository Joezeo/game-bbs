package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.mapper.IPMapper;
import com.joezeo.joefgame.dao.pojo.IP;
import com.joezeo.joefgame.dao.pojo.IPExample;
import com.joezeo.joefgame.potal.service.IPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Joezeo
 * @date 2020/4/8 13:22
 */
@Service
@Slf4j
public class IPServiceImpl implements IPService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IPMapper ipMapper;

    @Override
    public void recordIPByRedis(String ip) {
        String key = "ip-" + ip;
        if(redisUtil.hasKey(key)){
            // 该ip访问次数递增
            redisUtil.incr(key, 1);

            Integer cnt = (Integer) redisUtil.get(key);
            if(cnt >= 60){
                // 将该ip放入黑名单中
                redisUtil.sSet("blacklist", ip);
            }
        } else {
            redisUtil.set(key, 1, 60);
        }
    }

    @Override
    public void recordIPByMysql(String ip) {
        // 首先判断数据库中是否存在该ip记录
        IPExample example = new IPExample();
        example.createCriteria().andIpEqualTo(ip);
        List<IP> ips = ipMapper.selectByExample(example);

        if(ips == null || ips.size() == 0){
            IP ipIns = new IP();
            ipIns.setIp(ip);
            ipIns.setGmtCreate(System.currentTimeMillis());
            ipIns.setGmtModify(ipIns.getGmtCreate());
            int idx = ipMapper.insert(ipIns);
            if(idx != 1){
                log.error("数据库记录访问ip失败,ip=" + ip);
            }
        } else {
            IP ipIns = ips.get(0);
            ipIns.setGmtModify(System.currentTimeMillis());
            int idx = ipMapper.updateByPrimaryKey(ipIns);
            if(idx != 1){
                log.error("数据库更新访问ip失败,ip=" + ip);
            }
        }
    }

    @Override
    public boolean isInBlackList(String ip) {
        return redisUtil.sHasKey("blacklist", ip);
    }
}
