package com.joezeo.community.controller;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.pojo.Topic;
import com.joezeo.community.service.CommentService;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TopicController {

    @Autowired
    TopicService topicService;

    @Autowired
    CommentService commentService;

    @GetMapping("/topic/{id}")
    public String topic(@PathVariable("id") Long id) {
        if(!topicService.isExist(id)){ // 不存在抛异常
            throw new CustomizeException(CustomizeErrorCode.TOPIC_NOT_FOUND);
        }
        return "topic";
    }

    @PostMapping("/topic/getTopic")
    @ResponseBody
    public JsonResult<TopicDTO> getTopic(@RequestBody TopicDTO topicDTO){
        // 查询指定id的帖子
        topicDTO = topicService.queryById(topicDTO.getId());

        // 累加阅读数
        topicService.incVie(topicDTO.getId());

        return JsonResult.okOf(topicDTO);
    }

    @PostMapping("/topic/getComments")
    @ResponseBody
    public JsonResult<List<CommentDTO>> getComments(@RequestBody TopicDTO topicDTO){
        // 查询当前id帖子的全部评论
        List<CommentDTO> commentDTOS = commentService.listByParentId(topicDTO.getId(), CommentTypeEnum.QUESTION);

        return JsonResult.okOf(commentDTOS);
    }

    @PostMapping("/topic/like")
    @ResponseBody
    public JsonResult<TopicDTO> likeTopic(@RequestBody TopicDTO topicDTO){
        topicDTO = topicService.likeTopic(topicDTO.getId(), topicDTO.getUserid());
        return JsonResult.okOf(topicDTO);
    }
}
