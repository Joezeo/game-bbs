package com.joezeo.joefgame.potal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.mapper.TagMapper;
import com.joezeo.joefgame.dao.pojo.Tag;
import com.joezeo.joefgame.dao.pojo.TagExample;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.potal.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Tag> listTagsByCategory(Integer index) {
        if (index == null || index <= 0) {
            log.error("函数listTagsByCategory：参数index异常,=" + index);
            throw new ServiceException("参数index异常");
        }
        List<Tag> tags;
        // 先从redis中获取
        String key = "tag" + index.toString();
        if (redisUtil.hasKey(key)) {
            JSONArray json = (JSONArray) redisUtil.get(key);
            tags = JSON.parseArray(json.toJSONString(), Tag.class);
        } else { // redis中没有数据再从mysql数据库中获取
            TagExample example = new TagExample();
            example.createCriteria().andCategoryEqualTo(index);
            tags = tagMapper.selectByExample(example);
            // 存入redis中
            int diff = 60 * 60; // 如果获取时间差失败就存一个小时
            try {
                diff = TimeUtils.getDifftimeFromNextFour();
            } catch (ParseException e) {
                log.error("获取当前时间至第二天凌晨4点的时间差");
            }
            redisUtil.set(key, tags, diff);
        }
        if (tags == null || tags.size() == 0) {
            // 没有数据，返回一个空的list
            return new ArrayList<>();
        }
        return tags;
    }
}
