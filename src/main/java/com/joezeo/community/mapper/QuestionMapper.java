package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    int insert(Question question);

    List<Question> selectAll();
}
