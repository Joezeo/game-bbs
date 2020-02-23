package com.joezeo.joefgame.service;

import com.joezeo.joefgame.pojo.User;

public interface UserService {
    User queryUserByToken(String token);

    User queryByAccountid(String accountId);

    void createOrUpadate(User user);

    void signup(User user);

    void login(User user);
}
