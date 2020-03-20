package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CommentTypeEnum;
import com.joezeo.joefgame.common.dto.CommentDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.potal.service.CommentService;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicController {

    @Autowired
    TopicService topicService;

    @Autowired
    CommentService commentService;

    @PostMapping("/topic/getTopic")
    public JsonResult<TopicDTO> getTopic(@RequestBody TopicDTO topicDTO){
        // 查询指定id的帖子
        topicDTO = topicService.queryById(topicDTO.getId());

        // 累加阅读数
        topicService.incVie(topicDTO.getId());

        return JsonResult.okOf(topicDTO);
    }

    @PostMapping("/topic/getComments")
    public JsonResult<List<CommentDTO>> getComments(@RequestBody TopicDTO topicDTO){
        // 查询当前id帖子的全部评论
        List<CommentDTO> commentDTOS = commentService.listByParentId(topicDTO.getId(), CommentTypeEnum.QUESTION);

        return JsonResult.okOf(commentDTOS);
    }

    @PostMapping("/topic/like")
    public JsonResult<TopicDTO> likeTopic(@RequestBody TopicDTO topicDTO){
        topicDTO = topicService.likeTopic(topicDTO.getId(), topicDTO.getUserid());
        return JsonResult.okOf(topicDTO);
    }

    @PostMapping("/topic/unlike")
    public JsonResult<TopicDTO> unlikeTopic(@RequestBody TopicDTO topicDTO){
        topicDTO = topicService.unlikeTopic(topicDTO.getId(), topicDTO.getUserid());
        return JsonResult.okOf(topicDTO);
    }
}
