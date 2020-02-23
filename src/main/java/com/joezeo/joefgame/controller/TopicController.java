package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.CommentDTO;
import com.joezeo.joefgame.dto.JsonResult;
import com.joezeo.joefgame.dto.TopicDTO;
import com.joezeo.joefgame.enums.CommentTypeEnum;
import com.joezeo.joefgame.exception.CustomizeErrorCode;
import com.joezeo.joefgame.exception.CustomizeException;
import com.joezeo.joefgame.service.CommentService;
import com.joezeo.joefgame.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/topic/unlike")
    @ResponseBody
    public JsonResult<TopicDTO> unlikeTopic(@RequestBody TopicDTO topicDTO){
        topicDTO = topicService.unlikeTopic(topicDTO.getId(), topicDTO.getUserid());
        return JsonResult.okOf(topicDTO);
    }
}
