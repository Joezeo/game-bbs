package com.joezeo.community.controller;

import com.joezeo.community.pojo.User;
import com.joezeo.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String htmIndex(HttpServletRequest request){
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
        return "index";
    }
}
