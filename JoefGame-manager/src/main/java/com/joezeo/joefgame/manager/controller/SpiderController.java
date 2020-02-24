package com.joezeo.joefgame.manager.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.manager.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @PostMapping("/spideProxyIP")
    @ResponseBody
    public JsonResult spideProxyIP(){
        spiderService.spideProxyIP();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideUrl")
    @ResponseBody
    public JsonResult spideUrl() {
        spiderService.spideUrl();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideApp")
    @ResponseBody
    public JsonResult spideApp() {
        spiderService.spideApp();
        return JsonResult.okOf(null);
    }

    @PostMapping("/checkUrl")
    @ResponseBody
    public JsonResult checkUrl() {
        spiderService.checkUrl();
        return JsonResult.okOf(null);
    }

    @PostMapping("/checkApp")
    @ResponseBody
    public JsonResult checkApp() {
        spiderService.checkApp();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideSpecialPrice")
    @ResponseBody
    public JsonResult spideSpecialPrice() {
        spiderService.spideSpecialPrice();
        return JsonResult.okOf(null);
    }

}
