package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.NotificationDTO;
import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.NotificationService;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        return "profile";
    }

    @PostMapping("/profile/getTopics")
    @ResponseBody
    public JsonResult<PaginationDTO<TopicDTO>> getTopics(HttpSession session,
                                                         @RequestBody PaginationDTO<?> pagination) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }
        // 查询帖子
        PaginationDTO<TopicDTO> paginationDTO = topicService.listPage(user.getId(), pagination.getPage(), pagination.getSize());

        return JsonResult.okOf(paginationDTO);
    }

    @PostMapping("/profile/getNotify")
    @ResponseBody
    public JsonResult<NotificationDTO> getNotify(HttpSession session,
                                                 @RequestBody PaginationDTO<?> pagination) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        // 查询通知
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.listPage(user.getId(), pagination.getPage(), pagination.getSize());
        return JsonResult.okOf(paginationDTO);
    }
}


