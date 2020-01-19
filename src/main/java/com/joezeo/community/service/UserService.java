package com.joezeo.community.service;

import com.joezeo.community.pojo.User;

public interface UserService {
    User queryUserByToken(String token);

    User queryByAccountid(String accountId);

    void createOrUpadate(User user);
}
