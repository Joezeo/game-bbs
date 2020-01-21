package com.joezeo.community.mapper;

import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    /**
     * 用于累加阅读数
     */
    int incView(Question record);
}