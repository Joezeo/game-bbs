package com.joezeo.joefgame.common.mq;

import com.joezeo.joefgame.common.mq.message.ContentEnum;
import com.joezeo.joefgame.common.mq.message.IJoefMessage;
import com.joezeo.joefgame.common.mq.message.JoefSpecialPriceMessage;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.dao.pojo.SteamAppInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSupplier {
    @Autowired
    private RedisUtil redisUtil;

    public void putMessage(String supplierID, String consumerID, Object content){
        IJoefMessage<?> message = null;
        if(content instanceof SteamAppInfo){
            message = new JoefSpecialPriceMessage<SteamAppInfo>(supplierID, consumerID, (SteamAppInfo) content);
        } else {
            log.error("请检查程序代码，使用了暂不支持的消息队列内容类型：[支持的类型："+ ContentEnum.listType() +"]" +
                    "[使用的类型："+content.getClass()+"]");
        }

        boolean isOk = redisUtil.lPush(message.getQueueID(), message);
        if(!isOk){
            log.error("在消息队列中存入消息失败 message=" + message.toString());
        }
    }
}
