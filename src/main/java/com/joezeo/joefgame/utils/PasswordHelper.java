package com.joezeo.joefgame.utils;

import com.joezeo.joefgame.pojo.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordHelper {
    @Value("${shiro.hash.algorithm.name}")
    public String algorithm;
    @Value("${shiro.hash.iteration}")
    public Integer iteration;

    public void encryptPassword(User user){
        user.setSalt(UUID.randomUUID().toString());
        String encryptedPwd = new SimpleHash(algorithm, user.getPassword(),user.getSalt(), iteration).toString();
        user.setPassword(encryptedPwd);
    }
}
