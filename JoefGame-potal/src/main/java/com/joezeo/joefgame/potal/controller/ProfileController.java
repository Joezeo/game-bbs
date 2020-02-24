package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.potal.dto.NotificationDTO;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.service.NotificationService;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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


