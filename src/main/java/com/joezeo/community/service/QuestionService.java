package com.joezeo.community.service;

import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.pojo.Question;

import java.util.List;

public interface QuestionService {
    void addQuestion(Question question);

    List<QuestionDTO> list();
}
