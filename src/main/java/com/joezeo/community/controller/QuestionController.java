package com.joezeo.community.controller;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.service.CommentService;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        // 查询指定id的问题
        QuestionDTO questionDTO = questionService.queryById(id);

        // 查询当前id问题的全部评论
        List<CommentDTO> commentDTOS = commentService.listByParentId(id, CommentTypeEnum.QUESTION);

        // 累加阅读数
        questionService.incVie(questionDTO.getId());

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        return "question";
    }
}
