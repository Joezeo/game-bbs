package com.joezeo.joefgame.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerPageController {

    @GetMapping("/panel")
    public String htmPanel(){
        return "manager/panel";
    }

    @GetMapping("/spider")
    public String htmSpider(){
        return "manager/spider";
    }

    @GetMapping("/process")
    public String htmProcess(){
        return "manager/process";
    }

}
