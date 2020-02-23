package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.IndexDTO;
import com.joezeo.joefgame.dto.JsonResult;
import com.joezeo.joefgame.dto.PaginationDTO;
import com.joezeo.joefgame.dto.TopicDTO;
import com.joezeo.joefgame.pojo.User;
import com.joezeo.joefgame.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String htmIndex(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "index";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/forum")
    public String htmForum() {
        return "forum";
    }

    @GetMapping("/loadding")
    public String htmLoadding() {
        return "loadding";
    }

    @PostMapping("/list")
    @ResponseBody
    public JsonResult<IndexDTO> index(@RequestBody IndexDTO<TopicDTO> indexDTO) {
        PaginationDTO<TopicDTO> paginationDTO = topicService.listPage(indexDTO.getPage(), indexDTO.getSize(), indexDTO.getCondition(), indexDTO.getTab());

        indexDTO.setPagination(paginationDTO);

        return JsonResult.okOf(indexDTO);
    }

    @PostMapping("/getUser")
    @ResponseBody
    public JsonResult<User> getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");

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
