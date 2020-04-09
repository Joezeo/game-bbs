package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.potal.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/steam")
    public JsonResult<?> steam(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<SteamAppDTO> steams = searchService.searchSteam(condition, paginationDTO.getPage());
            return JsonResult.okOf(steams);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @PostMapping("/topic")
    public JsonResult<?> topic(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<TopicDTO> topics = searchService.searchTopic(condition, paginationDTO.getPage());
            return JsonResult.okOf(topics);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @PostMapping("/user")
    public JsonResult<?> user(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<UserDTO> users = searchService.searchUser(condition, paginationDTO.getPage());
            return JsonResult.okOf(users);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }
}
