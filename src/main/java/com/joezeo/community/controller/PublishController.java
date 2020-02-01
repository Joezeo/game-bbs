package com.joezeo.community.controller;

import com.joezeo.community.cache.TagCache;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.TagDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.pojo.Tag;
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

    // 获取新增帖子页面
    @GetMapping("/publish")
    public String htmPublish(Model model) {
        return "publish";
    }

    // 获取编辑帖子页面
    @GetMapping("/publish/{id}")
    public String htmEdit(@PathVariable(name = "id") Long id,
                       Model model) {
        model.addAttribute("id", id);
        return "publish";
    }

    // 获取标签信息
    @PostMapping("/publish/getTags")
    @ResponseBody
    public JsonResult<List<TagDTO>> getTags(){
        List<TagDTO> tagDTOS = tagCache.get();
        return JsonResult.okOf(tagDTOS);
    }

    // 获取需要编辑的帖子信息
    @PostMapping("/publish/getTopic")
    @ResponseBody
    public JsonResult<TopicDTO> getTopic(@RequestBody TopicDTO topicDTO){
        topicDTO = topicService.queryById(topicDTO.getId());
        return JsonResult.okOf(topicDTO);
    }


    // 发布帖子
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
        if(illegal.size() != 0) { // 存在非法项
            return JsonResult.errorOf(illegal);
        }

        topicService.createOrUpdate(topic);

        return JsonResult.okOf(null);
    }
}
