package com.joezeo.joefgame.common.mq.message;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Steam 应用降价的消息
 */
@Data
public class JoefSpecialPriceMessage<T> implements IJoefMessage<T>{
    private String queueid;
    private String userid; // consumer id 用户为消息消费者,每一个消费者拥有一个各自的消息队列
    private String appid; // supplier id 降价的Steam应用为生产者
    private T content;

    public JoefSpecialPriceMessage(@NotNull String supplierID,
                                   @NotNull String consumerID,
                                   T content){
        this.userid = consumerID;
        this.appid = supplierID;
        this.content = content;
        this.queueid = "mq-steam-" + consumerID;
    }

    @Override
    public String getQueueID() {
        return this.queueid;
    }


    @Override
    public String getSupplierID() {
        return this.appid;
    }


    @Override
    public String getConsumerID() {
        return this.userid;
    }


    @Override
    public T getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "JoefSpecialPriceMessage{" +
                "queueid='" + queueid + '\'' +
                ", userid='" + userid + '\'' +
                ", appid='" + appid + '\'' +
                ", steamAppInfo=" + content +
                '}';
    }
}
