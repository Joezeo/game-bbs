package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
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

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/steam")
    public JsonResult<PaginationDTO<SteamAppDTO>> steam(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<SteamAppDTO> steams = searchService.searchSteamBySolr(condition, paginationDTO.getPage());
            return JsonResult.okOf(steams);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @PostMapping("/topic")
    public JsonResult<PaginationDTO<TopicDTO>> topic(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<TopicDTO> topics = searchService.searchTopicBySolr(condition, paginationDTO.getPage());
            return JsonResult.okOf(topics);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @PostMapping("/user")
    public JsonResult<PaginationDTO<UserDTO>> user(@RequestBody PaginationDTO paginationDTO){
        try {
            String condition = URLDecoder.decode(paginationDTO.getCondition(), "UTF-8");
            PaginationDTO<UserDTO> users = searchService.searchUserBySolr(condition, paginationDTO.getPage());
            return JsonResult.okOf(users);
        } catch (UnsupportedEncodingException e) {
            log.error("解析前端传来的搜索条件异常，stackTrace：" + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.SEARCH_FAILED);
        }
    }
}
