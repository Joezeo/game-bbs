package com.joezeo.community.controller;

import com.joezeo.community.dto.IndexDTO;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.pojo.Topic;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String htmIndex() {
        return "index";
    }

    @GetMapping("/list")
    @ResponseBody
    public JsonResult<IndexDTO> index(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "tab", defaultValue = "question") String tab,
            @RequestParam(name = "condition", required = false) String condition) {
        IndexDTO<TopicDTO> indexDTO = new IndexDTO<>();
        PaginationDTO<TopicDTO> paginationDTO = topicService.listPage(page, size, condition, tab);
        indexDTO.setPagination(paginationDTO);
        indexDTO.setCondition(condition);
        indexDTO.setTab(tab);
        return JsonResult.okOf(indexDTO);
    }
}
