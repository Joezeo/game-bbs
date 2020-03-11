package com.joezeo.joefgame.web.config;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenidConfig {
    @Bean("consumerManager")
    public ConsumerManager consumerManager(){
        ConsumerManager manager=new ConsumerManager();
        manager.setAssociations(new InMemoryConsumerAssociationStore());
        manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        manager.setMaxAssocAttempts(0); // 设置为stateless模式，Steam要求
        return manager;
    }
}
