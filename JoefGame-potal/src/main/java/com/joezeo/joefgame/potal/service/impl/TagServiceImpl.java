package com.joezeo.joefgame.potal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.dao.mapper.TagMapper;
import com.joezeo.joefgame.dao.pojo.Tag;
import com.joezeo.joefgame.dao.pojo.TagExample;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.potal.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<Object> list = redisUtil.lGet(key, 0, -1);
            tags = new ArrayList<>();
            for (Object o : list) {
                JSONArray jarray = (JSONArray) o;
                Tag tag = JSON.parseObject(jarray.getJSONObject(1).toJSONString(), Tag.class);
                tags.add(tag);
            }
        } else { // redis中没有数据再从mysql数据库中获取
            TagExample example = new TagExample();
            example.createCriteria().andCategoryEqualTo(index);
            tags = tagMapper.selectByExample(example);
            // 存入redis中
            for (Tag tag : tags) { // 缓存一小时
                redisUtil.lSet(key, tag, 60 * 60);
            }
        }
        if (tags == null || tags.size() == 0) {
            // 没有数据，返回一个空的list
            return new ArrayList<>();
        }
        return tags;
    }
}
