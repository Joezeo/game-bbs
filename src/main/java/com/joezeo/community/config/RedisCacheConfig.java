package com.joezeo.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisCacheConfig {

    /**
     * 让redis使用线程当前的类加载器
     * 避免与devtools冲突
     */
    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory){
        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader()))
                .build();
    }
}
