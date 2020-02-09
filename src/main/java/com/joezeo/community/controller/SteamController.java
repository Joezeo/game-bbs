package com.joezeo.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/steam")
public class SteamController {

    @GetMapping("/apps")
    public String htmApps(){
        return "apps";
    }
}
