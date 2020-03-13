package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.potal.dto.SteamAppDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/steam")
    @ResponseBody
    public JsonResult<List<SteamAppDTO>> steam(@RequestBody SteamAppDTO steamAppDTO){
        try {
            String condition = URLDecoder.decode(steamAppDTO.getCondition(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
        return JsonResult.okOf(null);
    }

    @PostMapping("/topic")
    @ResponseBody
    public JsonResult<List<TopicDTO>> topic(@RequestBody TopicDTO topicDTO){
        try {
            String condition = URLDecoder.decode(topicDTO.getCondition(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
        return JsonResult.okOf(null);
    }

    @PostMapping("/user")
    @ResponseBody
    public JsonResult<List<UserDTO>> user(@RequestBody UserDTO userDTO){
        try {
            String condition = URLDecoder.decode(userDTO.getCondition(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
        return JsonResult.okOf(null);
    }
}
