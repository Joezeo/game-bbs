package com.joezeo.community.controller;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.service.QuestionService;
import com.joezeo.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String htmIndex(HttpServletRequest request,
                           Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "5") Integer size) {
        // 查询问题
        PaginationDTO paginationDTO = questionService.listPage(page, size);

        model.addAttribute("pagination", paginationDTO);
        return "index";
    }
}
