package com.joezeo.community.service.impl;

import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        if(user == null){
            System.out.println("参数为空");
        }

        int count = userMapper.insert(user);
        if(count != 1){
            System.out.println("错误");
        }
    }

    @Override
    public User queryUserByToken(String token) {
        if(token == null || "".equals(token)){
            System.out.println("参数token为空");
        }

        User user = userMapper.selectByToken(token);
        if(user == null){
            System.out.println("获取user信息失败");
        }
        return user;
    }
}
