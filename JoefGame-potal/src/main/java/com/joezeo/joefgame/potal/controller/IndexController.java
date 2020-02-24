package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.dto.IndexDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class IndexController {
    @Autowired
    private TopicService topicService;

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
