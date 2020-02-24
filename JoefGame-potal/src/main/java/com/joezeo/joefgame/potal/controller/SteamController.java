package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.potal.dto.AppsDTO;
import com.joezeo.joefgame.potal.dto.HistoryPriceDTO;
import com.joezeo.joefgame.potal.dto.SteamAppDTO;
import com.joezeo.joefgame.potal.service.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/steam")
public class SteamController {

    @Autowired
    private SteamService steamService;
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
