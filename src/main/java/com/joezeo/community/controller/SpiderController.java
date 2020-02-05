package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @PostMapping("/spideUrl")
    @ResponseBody
    public JsonResult spideUrl(){
        spiderService.spideUrl();
        return JsonResult.okOf(null);
    }
}
