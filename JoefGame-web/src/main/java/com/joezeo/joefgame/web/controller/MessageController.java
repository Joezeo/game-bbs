package com.joezeo.joefgame.web.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.common.mq.MessageConsumer;
import com.joezeo.joefgame.common.mq.message.IJoefMessage;
import com.joezeo.joefgame.dao.pojo.SteamAppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于提供消息队列相关服务
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageConsumer messageConsumer;

    @PostMapping("/doGet")
    public JsonResult<SteamAppInfo> doGet(@RequestBody UserDTO userDTO){
        // 获取Steam应用降价消息
        IJoefMessage<SteamAppInfo> message = messageConsumer.getMessage(userDTO.getUserid(), SteamAppInfo.class);
        SteamAppInfo steamAppInfo = message.getContent();
        return JsonResult.okOf(steamAppInfo);
    }
}
