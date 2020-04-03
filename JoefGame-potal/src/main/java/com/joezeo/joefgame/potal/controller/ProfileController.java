package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.dto.NotificationDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.potal.service.NotificationService;
import com.joezeo.joefgame.potal.service.TopicService;
import com.joezeo.joefgame.potal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping("/getPersonal")
    public JsonResult<UserDTO> getPersonal(HttpSession session){
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        return JsonResult.okOf(userDTO);
    }

    @PostMapping("/getTopics")
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

    @PostMapping("/getNotify")
    public JsonResult<PaginationDTO<NotificationDTO>> getNotify(HttpSession session,
                                                 @RequestBody PaginationDTO<?> pagination) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        // 查询通知
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.listPage(user.getId(), pagination.getPage(), pagination.getSize());
        return JsonResult.okOf(paginationDTO);
    }

    @PostMapping("/uploadAvatar")
    public JsonResult<?> uploadAvatar(HttpServletRequest request, HttpSession session){
        MultipartHttpServletRequest mReq = (MultipartHttpServletRequest) request;
        MultipartFile avatar = mReq.getFile("avatar");

        UserDTO user = (UserDTO) session.getAttribute("user");
        String newAvatarUrl = userService.updateAvatar(avatar, user.getId());

        // Session中修改头像地址至新的头像地址
        user.setAvatarUrl(newAvatarUrl);
        session.setAttribute("user", user);
        return JsonResult.okOf(null);
    }
}


