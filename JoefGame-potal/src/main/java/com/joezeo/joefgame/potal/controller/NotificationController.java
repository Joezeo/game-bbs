package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notification/{id}")
    public JsonResult<Long> notification(@PathVariable("id") Long id, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        // 读取该条通知 , 修改通知status、获取该条通知所属帖子id
        Long topicid = notificationService.readNotification(id, user.getId());

        return JsonResult.okOf(topicid);
    }

    @PostMapping("/notification/allRead")
    public JsonResult allRead(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        notificationService.readAll(user.getId());
        return JsonResult.okOf(null);
    }
}
