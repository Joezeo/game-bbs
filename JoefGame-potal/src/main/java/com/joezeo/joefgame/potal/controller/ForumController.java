package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.potal.dto.ForumDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForumController {
    @Autowired
    private TopicService topicService;

    @PostMapping("/list")
    @ResponseBody
    public JsonResult<ForumDTO> index(@RequestBody ForumDTO<TopicDTO> forumDTO) {
        PaginationDTO<TopicDTO> paginationDTO = topicService.listPage(forumDTO.getPage(), forumDTO.getSize(), forumDTO.getTab());

        forumDTO.setPagination(paginationDTO);

        return JsonResult.okOf(forumDTO);
    }
}
