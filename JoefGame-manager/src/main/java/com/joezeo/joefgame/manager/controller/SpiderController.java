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
    public JsonResult spideProxyIP(){
        spiderService.spideProxyIP();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideUrl")
    public JsonResult spideUrl() {
        spiderService.spideUrl();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideApp")
    public JsonResult spideApp() {
        spiderService.spideApp();
        return JsonResult.okOf(null);
    }

    @PostMapping("/checkUrl")
    public JsonResult checkUrl() {
        spiderService.checkUrl();
        return JsonResult.okOf(null);
    }

    @PostMapping("/checkApp")
    public JsonResult checkApp() {
        spiderService.checkApp();
        return JsonResult.okOf(null);
    }

    @PostMapping("/spideSpecialPrice")
    public JsonResult spideSpecialPrice() {
        spiderService.spideSpecialPrice();
        return JsonResult.okOf(null);
    }

}
