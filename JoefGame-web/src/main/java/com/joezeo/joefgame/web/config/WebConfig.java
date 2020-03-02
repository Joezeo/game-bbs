package com.joezeo.joefgame.web.config;

import com.joezeo.joefgame.web.interceptor.AccessControIntercepter;
import com.joezeo.joefgame.web.interceptor.SessionInterceptor;
import com.joezeo.joefgame.web.interceptor.SpiderAccessControlIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Autowired
    private AccessControIntercepter accessControIntercepter;
    @Autowired
    private SpiderAccessControlIntercepter spiderAccessControlIntercepter;
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(accessControIntercepter).addPathPatterns("/**");
        registry.addInterceptor(spiderAccessControlIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/bootstrap/**")
                .excludePathPatterns("/imgs/**")
                .excludePathPatterns("/loadding")
                .excludePathPatterns("/jquery/jquery-3.4.1.js")
                .excludePathPatterns("/authAccess");
    }
}