package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public JsonResult publish(Question question, HttpSession session){
        JsonResult result = null;

        try{
            User user = (User) session.getAttribute("user");
            if(user == null){
                result = new JsonResult(false, "用户未登录");
                return result;
            }
            question.setUserid(user.getId());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModify(question.getGmtCreate());

            questionService.addQuestion(question);
            result = new JsonResult(true, "Ok");
        } catch (Exception e){
            e.printStackTrace();
            result = new JsonResult(e);
        }

        return result;
    }
}
