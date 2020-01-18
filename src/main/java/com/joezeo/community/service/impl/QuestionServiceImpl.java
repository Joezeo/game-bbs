package com.joezeo.community.service.impl;

import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.QuestionMapper;
import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

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

    @Override
    public List<QuestionDTO> list() {
        List<Question> questions = questionMapper.selectAll();
        if(questions == null){
            throw new ServiceException("获取问题失败");
        }

        List<QuestionDTO> list = new ArrayList<>();
        for(Question question : questions){
            User user = userMapper.selectById(question.getUserid());
            if(user == null){
                throw new ServiceException("获取用户失败");
            }

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            list.add(questionDTO);
        }

        return list;
    }
}
