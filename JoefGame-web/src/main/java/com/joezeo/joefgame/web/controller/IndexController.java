package com.joezeo.joefgame.web.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.common.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class IndexController {

    @PostMapping("/getUser")
    @ResponseBody
    public JsonResult<User> getUser(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        return JsonResult.okOf(user);
    }

    /**
     * 获取当前session用户的未读通知数量
     *
     * @param session
     * @return
     */
    @PostMapping("/getUnreadCount")
    @ResponseBody
    public JsonResult<Integer> getUnreadCount(HttpSession session) {
        Integer unreadCount = (Integer) session.getAttribute("unreadCount");

        return JsonResult.okOf(unreadCount);
    }
}
