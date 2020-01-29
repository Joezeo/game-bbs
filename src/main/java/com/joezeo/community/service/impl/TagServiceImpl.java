package com.joezeo.community.service.impl;

import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.TagMapper;
import com.joezeo.community.pojo.Tag;
import com.joezeo.community.pojo.TagExample;
import com.joezeo.community.dao.RedisUtils;
import com.joezeo.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<Tag> listTagsByCategory(Integer index) {
        if(index == null || index <= 0){
            throw new ServiceException("参数index异常");
        }
        List<Tag> tags;
        // 先从redis中获取
        String key = "tag" + index.toString();
        if(redisUtils.hasKey(key)){
            List<Object> list = redisUtils.lGet(key, 0 , -1);
            tags = new ArrayList<>();
            for (Object o : list) {
                tags.add((Tag) o);
            }
        } else { // redis中没有数据再从mysql数据库中获取
            TagExample example = new TagExample();
            example.createCriteria().andCategoryEqualTo(index);
            tags = tagMapper.selectByExample(example);
            // 存入redis中
            for (Tag tag : tags) {
                redisUtils.lSet(key, tag);
            }
        }
        if(tags == null || tags.size()==0){
            // 没有数据，返回一个空的list
            return new ArrayList<>();
        }
        return tags;
    }
}
