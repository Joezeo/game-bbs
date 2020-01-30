package com.joezeo.community.controller;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String htmIndex(HttpServletRequest request,
                           Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "5") Integer size,
                           @RequestParam(name = "tab", defaultValue = "question") String tab,
                           @RequestParam(name = "condition", required = false) String condition) {
        // 查询帖子
        PaginationDTO paginationDTO = topicService.listPage(page, size, condition, tab);

        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("condition", condition);
        model.addAttribute("tab", tab);
        return "index";
    }
}
