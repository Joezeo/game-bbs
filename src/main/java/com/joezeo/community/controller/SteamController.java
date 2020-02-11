package com.joezeo.community.controller;

import com.joezeo.community.dto.AppsDTO;
import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.SteamAppDTO;
import com.joezeo.community.pojo.SteamAppInfo;
import com.joezeo.community.service.SteamService;
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
        PaginationDTO<SteamAppInfo> paginationDTO = steamService.listApps(appsDTO.getPage(), appsDTO.getSize(), appsDTO.getAppType());
        appsDTO.setPagination(paginationDTO);
        return JsonResult.okOf(appsDTO);
    }

    @PostMapping("/getApp")
    @ResponseBody
    public JsonResult<SteamAppDTO> getApp(@RequestBody SteamAppDTO appDTO){
        appDTO = steamService.queryApp(appDTO.getAppid(), appDTO.getType());
        return JsonResult.okOf(appDTO);
    }
}
