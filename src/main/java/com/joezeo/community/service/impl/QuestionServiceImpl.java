package com.joezeo.community.service.impl;

import com.joezeo.community.mapper.QuestionMapper;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void addQuestion(Question question) {
        if (question == null) {
            throw new RuntimeException("参数question不可为null");
        }

        int count = questionMapper.insert(question);
        if(count != 1){
            throw new RuntimeException("发布新问题失败");
        }
    }
}
