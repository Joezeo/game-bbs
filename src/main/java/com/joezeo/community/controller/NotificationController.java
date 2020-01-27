package com.joezeo.community.controller;

import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable("id") Long id, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        // 读取该条通知 , 修改通知status、获取该条通知所属问题id
        Long questionid = notificationService.readNotification(id, user.getId());

        return "redirect:/question/" + questionid;
    }
}
