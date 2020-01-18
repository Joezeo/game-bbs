package com.joezeo.community.controller;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import com.joezeo.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String htmIndex(HttpServletRequest request,
                           Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "2") Integer size){
        try{
            User user = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if("token".equals(cookie.getName())){
                        user = userService.queryUserByToken(cookie.getValue());
                    }
                }
            }
            if(user != null){
                request.getSession().setAttribute("user", user);
            }

            // 查询问题
            PaginationDTO paginationDTO = questionService.listPage(page, size);

            model.addAttribute("pagination", paginationDTO);
        } catch (Exception se){
            se.printStackTrace();
        }
        return "index";
    }
}
