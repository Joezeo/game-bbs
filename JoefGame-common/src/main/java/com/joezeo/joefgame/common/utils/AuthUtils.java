package com.joezeo.joefgame.common.utils;

import java.util.Random;

public class AuthUtils {
    private AuthUtils(){
    }

    // 随机生成一个四位数字的验证码
    public static String generateAuthcode(){
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<4; i++){
            int random =  new Random().nextInt(10);
            sb.append(random);
        }

        return sb.toString();
    }
}
