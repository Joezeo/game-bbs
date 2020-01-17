package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public JsonResult publish(Question question, HttpSession session){
        JsonResult result = null;

        try{
            User user = (User) session.getAttribute("user");
            if(user == null){
                result = new JsonResult(false, "用户未登录");
                return result;
            }
            question.setUserid(user.getId());
            question.setComment_count(0);
            question.setLike_count(0);
            question.setView_count(0);
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modify(question.getGmt_create());

            questionService.addQuestion(question);
            result = new JsonResult(true, "Ok");
        } catch (Exception e){
            e.printStackTrace();
            result = new JsonResult(e);
        }

        return result;
    }
}
