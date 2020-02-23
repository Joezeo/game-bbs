package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.JsonResult;
import com.joezeo.joefgame.exception.CustomizeErrorCode;
import com.joezeo.joefgame.exception.CustomizeException;
import com.joezeo.joefgame.pojo.User;
import com.joezeo.joefgame.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

        // 读取该条通知 , 修改通知status、获取该条通知所属帖子id
        Long topicid = notificationService.readNotification(id, user.getId());

        return "redirect:/topic/" + topicid;
    }

    @PostMapping("/notification/allRead")
    @ResponseBody
    public JsonResult allRead(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        notificationService.readAll(user.getId());
        return JsonResult.okOf(null);
    }
}
