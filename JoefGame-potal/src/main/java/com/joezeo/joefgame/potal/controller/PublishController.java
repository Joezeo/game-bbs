package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.dao.pojo.Topic;
import com.joezeo.joefgame.potal.cache.TagCache;
import com.joezeo.joefgame.potal.dto.TagDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PublishController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagCache tagCache;

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
        UserDTO user = (UserDTO) session.getAttribute("user");
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
