package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuestionMapper {
    int insert(Question question);
}
