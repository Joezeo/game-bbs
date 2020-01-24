package com.joezeo.community.controller;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          Model model,
                          HttpSession session) {
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
        }

        // 查询问题
        User user = (User) session.getAttribute("user");
        PaginationDTO paginationDTO = questionService.listPage(user.getId(), page, size);

        model.addAttribute("pagination", paginationDTO);
        return "profile";
    }
}
