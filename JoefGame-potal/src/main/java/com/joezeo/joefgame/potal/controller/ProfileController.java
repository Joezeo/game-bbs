package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.potal.dto.NotificationDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.NotificationService;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ProfileController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/profile/getPersonal")
    public JsonResult<UserDTO> getPersonal(HttpSession session){
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        return JsonResult.okOf(userDTO);
    }

    @PostMapping("/profile/getTopics")
    public JsonResult<PaginationDTO<TopicDTO>> getTopics(HttpSession session,
                                                         @RequestBody PaginationDTO<?> pagination) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }
        // 查询帖子
        PaginationDTO<TopicDTO> paginationDTO = topicService.listPage(user.getId(), pagination.getPage(), pagination.getSize());

        return JsonResult.okOf(paginationDTO);
    }

    @PostMapping("/profile/getNotify")
    public JsonResult<NotificationDTO> getNotify(HttpSession session,
                                                 @RequestBody PaginationDTO<?> pagination) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        // 查询通知
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.listPage(user.getId(), pagination.getPage(), pagination.getSize());
        return JsonResult.okOf(paginationDTO);
    }
}


