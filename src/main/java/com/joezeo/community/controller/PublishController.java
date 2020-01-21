package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.QuestionDTO;
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

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        try{
            QuestionDTO questionDTO = questionService.queryById(id);
            model.addAttribute("title", questionDTO.getTitle());
            model.addAttribute("description", questionDTO.getDescription());
            model.addAttribute("tag", questionDTO.getTag());
            model.addAttribute("id", questionDTO.getId());
        } catch (Exception e){
            e.printStackTrace();
        }
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

            questionService.createOrUpdate(question);
            result = new JsonResult(true, "Ok");
        } catch (Exception e){
            e.printStackTrace();
            result = new JsonResult(e);
        }

        return result;
    }
}
