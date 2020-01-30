package com.joezeo.community.controller;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.service.CommentService;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TopicController {

    @Autowired
    TopicService topicService;

    @Autowired
    CommentService commentService;

    @GetMapping("/topic/{id}")
    public String topic(@PathVariable(name = "id") Long id,
                           Model model) {
        // 查询指定id的帖子
        TopicDTO topicDTO = topicService.queryById(id);

        // 查询当前id帖子的全部评论
        List<CommentDTO> commentDTOS = commentService.listByParentId(id, CommentTypeEnum.QUESTION);

        // 累加阅读数
        topicService.incVie(topicDTO.getId());

        model.addAttribute("topic", topicDTO);
        model.addAttribute("comments", commentDTOS);
        return "topic";
    }
}
