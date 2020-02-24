package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
public class AuthorizeController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public JsonResult<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userService.login(user);

        Cookie cookie = new Cookie("token", token);
        if(userDTO.getRememberMe()){
            cookie.setMaxAge(60 * 60 * 24 * 7); // 存储7天登录信息
        }
        response.addCookie(cookie);

        return JsonResult.okOf(null);
    }

    @PostMapping("/logout")
    @ResponseBody
    public JsonResult<?> logout(HttpServletResponse response,
                         HttpSession session){
        // 移除cookie
        Cookie token = new Cookie("token", null);
        response.addCookie(token);
        token.setMaxAge(0);
        token.setPath("/");

        // 移除session
        session.removeAttribute("user");

        return JsonResult.okOf(null);
    }

    @PostMapping("/signup")
    @ResponseBody
    public JsonResult signup(@RequestBody UserDTO userDTO, HttpServletResponse response){
        User user =new User();
        BeanUtils.copyProperties(userDTO, user);

        String token = UUID.randomUUID().toString();
        user.setToken(token);

        userService.signup(user);

        response.addCookie(new Cookie("token", token));
        return JsonResult.okOf(null);
    }

    @PostMapping("/authAccess")
    @ResponseBody
    public JsonResult authAccess(HttpServletResponse response){

        Cookie access = new Cookie("__access", UUID.randomUUID().toString());
        access.setMaxAge(60 * 60 * 24); // cookie储存一天
        response.addCookie(access);
        return JsonResult.okOf(null);
    }
}
