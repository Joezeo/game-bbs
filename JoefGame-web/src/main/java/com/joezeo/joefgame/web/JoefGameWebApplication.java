package com.joezeo.joefgame.web;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//集成activiti工作流 增加exclude = SecurityAutoConfiguration.class
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.joezeo.joefgame.*.mapper")
@ComponentScan("com.joezeo.joefgame")
@EnableScheduling
public class JoefGameWebApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","Asia/Shanghai");
        SpringApplication.run(JoefGameWebApplication.class, args);
    }

}
