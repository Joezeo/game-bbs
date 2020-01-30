package com.joezeo.community.controller;

import com.joezeo.community.cache.TagCache;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.pojo.Topic;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagCache tagCache;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tagDTOS", tagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        TopicDTO topicDTO = topicService.queryById(id);
        model.addAttribute("title", topicDTO.getTitle());
        model.addAttribute("description", topicDTO.getDescription());
        model.addAttribute("tag", topicDTO.getTag());
        model.addAttribute("id", topicDTO.getId());
        model.addAttribute("tagDTOS", tagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    @ResponseBody
    public JsonResult publish(@RequestBody Topic topic, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return JsonResult.errorOf(CustomizeErrorCode.USER_NOT_LOGIN);
        }
        topic.setUserid(user.getId());

        // 判断标签是否存在非法项
        List<String> illegal = tagCache.check(topic.getTag());
        if(illegal.size() != 0){ // 存在非法项
            return JsonResult.errorOf(illegal);
        }

        topicService.createOrUpdate(topic);

        return JsonResult.okOf(null);
    }
}
