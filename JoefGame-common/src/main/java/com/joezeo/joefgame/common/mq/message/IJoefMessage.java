package com.joezeo.joefgame.common.mq.message;

/**
 * 参数 supplierID、consumerID、content由构造函数传入，不提供set方法
 * queueID 在构造器中生成
 *
 * 泛型 T：content 类型
 */
public interface IJoefMessage<T>{
    String getQueueID();

    String getSupplierID();

    String getConsumerID();

    T getContent();
}
