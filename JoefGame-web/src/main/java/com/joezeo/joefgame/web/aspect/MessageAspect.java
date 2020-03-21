package com.joezeo.joefgame.web.aspect;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.common.mq.MessageConsumer;
import com.joezeo.joefgame.dao.pojo.SteamAppInfo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息队列切面
 * 用于检查该用户消息队列中是否存在消息
 * 执行消息检查的页面为所有需要进行获取用户的页面
 * 即所有异步请求了/getUser的页面
 */
@Aspect
@Component
public class MessageAspect {

    @Autowired
    private MessageConsumer messageConsumer;

    /**
     * 切点为com.joezeo.joefgame.web.controller.IndexController类中
     * 返回值类型为com.joezeo.joefgame.common.dto.JsonResult，名为getUser()的方法
     */
    @Pointcut("execution(public com.joezeo.joefgame.common.dto.JsonResult com.joezeo.joefgame.web.controller.IndexController.getUser(..))")
    public void getUserServer() {
    }

    @AfterReturning(pointcut = "getUserServer()", returning = "jsonResult")
    public void checkMessage(JsonResult<UserDTO> jsonResult) {
        /*
        检查消息队列
         */
        UserDTO user = jsonResult.getData();
        if (user != null) { // 用户登录了
            boolean hasMessage = messageConsumer.hasMessage("" + user.getId(), SteamAppInfo.class);
            if (hasMessage) { // 用户存在未读消息
                jsonResult.setHasMessage(hasMessage);
            }
        }
    }
}
