package com.joezeo.community.service.impl;

import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.TagMapper;
import com.joezeo.community.pojo.Tag;
import com.joezeo.community.pojo.TagExample;
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

    @Override
    public List<Tag> listTagsByCategory(Integer index) {
        if(index == null || index <= 0){
            throw new ServiceException("参数index异常");
        }

        TagExample example = new TagExample();
        example.createCriteria().andCategoryEqualTo(index);
        List<Tag> tags = tagMapper.selectByExample(example);
        if(tags == null || tags.size()==0){
            // 没有数据，返回一个空的list
            return new ArrayList<>();
        }
        return tags;
    }
}
