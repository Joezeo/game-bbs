package com.joezeo.joefgame.web.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
    @Bean("client4Steam")
    public OkHttpClient initClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60 * 10, TimeUnit.SECONDS)
                .writeTimeout(60 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    // 设置延迟时间delay 防止ip被ban
                    int delay = 500;
                    try {
                        System.out.println("爬虫 delay 500毫秒");
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return chain.proceed(chain.request());
                });

        return builder.build();
    }
}
