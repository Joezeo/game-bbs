package com.joezeo.joefgame.common.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
public class MessageConsumer {

    @Autowired
    private RedisUtil redisUtil;

    public boolean hasMessage(String consumerID, Class<?> contentType){
        if (!ContentEnum.hasType(contentType)) {
            log.error("请检查程序代码，使用了暂不支持的消息队列内容类型：[支持的类型：" + ContentEnum.listType() + "]" +
                    "[使用的类型：" + contentType + "]");
            return false;
        }

        String queueid = "";
        if(contentType.equals(SteamAppInfo.class)){
            queueid = "mq-steam-" + consumerID;
        }

        if (!redisUtil.hasKey(queueid)) {
            return false;
        }

        if(redisUtil.lGetListSize(queueid) > 0){
            return true;
        }

        return false;
    }

    /*
    每一个用户只会访问各自的消息队列所以不存在线程安全的问题
     */
    public <T> IJoefMessage<T> getMessage(String consumerID, Class<T> contentType) {
        if (!ContentEnum.hasType(contentType)) {
            log.error("请检查程序代码，使用了暂不支持的消息队列内容类型：[支持的类型：" + ContentEnum.listType() + "]" +
                    "[使用的类型：" + contentType + "]");
            return null;
        }

        String queueid = "";
        if(contentType.equals(SteamAppInfo.class)){
            queueid = "mq-steam-" + consumerID;
        }

        if (!redisUtil.hasKey(queueid)) {
            return null;
        }

        // 尝试获取消息
        JSONObject o = (JSONObject) redisUtil.lPop(queueid);
        IJoefMessage<T> message = JSON.parseObject(o.toJSONString(), new TypeReference<JoefSpecialPriceMessage<T>>() {
        });
        return message;
    }
}
