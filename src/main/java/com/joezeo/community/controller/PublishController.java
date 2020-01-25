package com.joezeo.community.controller;

import com.joezeo.community.cache.TagCache;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

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
        QuestionDTO questionDTO = questionService.queryById(id);
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id", questionDTO.getId());
        model.addAttribute("tagDTOS", tagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    @ResponseBody
    public JsonResult publish(Question question, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return JsonResult.errorOf(CustomizeErrorCode.USER_NOT_LOGIN);
        }
        question.setUserid(user.getId());

        // 判断标签是否存在非法项
        List<String> illegal = tagCache.check(question.getTag());
        if(illegal.size() != 0){ // 存在非法项
            return JsonResult.errorOf(illegal);
        }

        questionService.createOrUpdate(question);

        return JsonResult.okOf(null);
    }
}
