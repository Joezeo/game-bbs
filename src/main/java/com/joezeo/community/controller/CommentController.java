package com.joezeo.community.controller;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public JsonResult comment(@RequestBody CommentDTO commentDTO,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return JsonResult.errorOf(CustomizeErrorCode.USER_NOT_LOGIN);
        }

        commentService.addComment(commentDTO, user.getId());

        return JsonResult.okOf(null);
    }
}
