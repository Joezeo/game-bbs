package com.joezeo.community.service.impl;

import com.joezeo.community.exception.ServiceException;
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
        if (user == null) {
            throw new ServiceException("参数user为空");
        }

        int count = userMapper.insert(user);
        if (count != 1) {
            throw new ServiceException("保存用户失败");
        }
    }

    @Override
    public User queryUserByToken(String token) {
        if (token == null || "".equals(token)) {
            throw new ServiceException("参数token为空");
        }

        User user = userMapper.selectByToken(token);
        if (user == null) {
            throw new ServiceException("by token, 获取user信息失败");
        }
        return user;
    }

    @Override
    public User queryByAccountid(String accountId) {
        if (accountId == null || "".equals(accountId)) {
            throw new ServiceException("参数accountId为空");
        }

        User user = userMapper.selectByAccountid(accountId);
        if (user == null) {
            throw new ServiceException("by accountid, 获取user信息失败");
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        if (user == null) {
            throw new ServiceException("参数user为空");
        }

        int count = userMapper.updateByIdSelective(user);
        if (count != 1) {
            throw new ServiceException("更新用户信息失败");
        }
    }
}
