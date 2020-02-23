package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.*;
import com.joezeo.joefgame.service.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Controller
@RequestMapping("/steam")
public class SteamController {

    @Autowired
    private SteamService steamService;

    @GetMapping("/apps")
    public String htmApps(){
        return "apps";
    }

    @GetMapping("/app/{appid}")
    public String htmApp(){
        return "app";
    }

    @PostMapping("/list")
    @ResponseBody
    public JsonResult<AppsDTO> listApps(@RequestBody AppsDTO appsDTO){
        PaginationDTO<?> paginationDTO = steamService.listApps(appsDTO.getPage(), appsDTO.getSize(), appsDTO.getAppType());
        appsDTO.setPagination(paginationDTO);
        return JsonResult.okOf(appsDTO);
    }

    @PostMapping("/getApp")
    @ResponseBody
    public JsonResult<SteamAppDTO> getApp(@RequestBody SteamAppDTO appDTO){
        appDTO = steamService.queryApp(appDTO.getAppid(), appDTO.getType());
        return JsonResult.okOf(appDTO);
    }

    @PostMapping("getPrice")
    @ResponseBody
    public JsonResult<HistoryPriceDTO> getPrice(@RequestBody HistoryPriceDTO historyPriceDTO){
        historyPriceDTO = steamService.queryHistoryPrice(historyPriceDTO.getAppid(), historyPriceDTO.getType());
        return JsonResult.okOf(historyPriceDTO);
    }
}
