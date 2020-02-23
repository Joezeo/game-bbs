package com.joezeo.joefgame;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//集成activiti工作流 增加exclude = SecurityAutoConfiguration.class
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.joezeo.joefgame.mapper")
@EnableScheduling
public class JoefgameApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","Asia/Shanghai");
        SpringApplication.run(JoefgameApplication.class, args);
    }

}
