package com.joezeo.joefgame.potal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String htmHome(){
        return "home";
    }
}