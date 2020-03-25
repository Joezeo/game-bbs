package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.*;
import com.joezeo.joefgame.dao.pojo.UserFavoriteApp;
import com.joezeo.joefgame.potal.service.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/steam")
public class SteamController {

    @Autowired
    private SteamService steamService;

    @PostMapping("/list")
    public JsonResult<AppsDTO> listApps(@RequestBody AppsDTO appsDTO){
        PaginationDTO<?> paginationDTO = steamService.listApps(appsDTO.getPage(), appsDTO.getSize(), appsDTO.getAppType());
        appsDTO.setPagination(paginationDTO);
        return JsonResult.okOf(appsDTO);
    }

    @PostMapping("/getApp")
    public JsonResult<SteamAppDTO> getApp(@RequestBody SteamAppDTO appDTO){
        appDTO = steamService.queryApp(appDTO.getAppid(), appDTO.getType());
        return JsonResult.okOf(appDTO);
    }

    @PostMapping("getPrice")
    public JsonResult<HistoryPriceDTO> getPrice(@RequestBody HistoryPriceDTO historyPriceDTO){
        historyPriceDTO = steamService.queryHistoryPrice(historyPriceDTO.getAppid(), historyPriceDTO.getType());
        return JsonResult.okOf(historyPriceDTO);
    }

    @PostMapping("getFavorites")
    public JsonResult<UserDTO>  getFavorites(HttpSession session){
        // 用户是否已经登录前端已经判断，故无需再次判断
        UserDTO user = (UserDTO) session.getAttribute("user");
        List<UserFavoriteApp> favorites = steamService.getFavorites(user.getId());
        user.setFavorites(favorites);
        return JsonResult.okOf(user);
    }

    @PostMapping("favoriteApp")
    public JsonResult<UserDTO> favoriteApp(@RequestBody SteamAppDTO steamAppDTO, HttpSession session){
        // 用户是否已经登录前端已经判断，故无需再次判断
        UserDTO user = (UserDTO) session.getAttribute("user");
        List<UserFavoriteApp> favorites = steamService.favoriteApp(user.getId(), steamAppDTO.getAppid(), steamAppDTO.getType());
        user.setFavorites(favorites);
        return JsonResult.okOf(user);
    }

    @PostMapping("unFavoriteApp")
    public JsonResult<UserDTO> unFavoriteApp(@RequestBody SteamAppDTO steamAppDTO, HttpSession session){
        // 用户是否已经登录前端已经判断，故无需再次判断
        UserDTO user = (UserDTO) session.getAttribute("user");
        List<UserFavoriteApp> favorites = steamService.unFavoriteApp(user.getId(), steamAppDTO.getAppid(), steamAppDTO.getType());
        user.setFavorites(favorites);
        return JsonResult.okOf(user);
    }

    @PostMapping("/getPlayers")
    public JsonResult<Integer> getPlayers(@RequestBody SteamAppDTO steamAppDTO){
        Integer players = steamService.getPlayers(steamAppDTO.getAppid());
        return JsonResult.okOf(players);
    }
}
