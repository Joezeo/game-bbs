package com.joezeo.community.config;

import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UCloudConfig {

    @Value("${ucloud.ufile.public.key}")
    private String publicKey;

    @Value("${ucloud.ufile.private.key}")
    private String privateKey;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Bean(name = "objectAuthorization")
    public ObjectAuthorization createObjectAuthorization(){
        return new UfileObjectLocalAuthorization(publicKey, privateKey);
    }

    @Bean(name = "objectConfig")
    public ObjectConfig createObjectConfig(){
        return new ObjectConfig(region, suffix);
    }
}
