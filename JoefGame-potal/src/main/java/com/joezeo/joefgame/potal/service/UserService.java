package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.dao.pojo.User;

public interface UserService {
    User queryUserByToken(String token);

    User queryByAccountid(String accountId);

    void createOrUpadate(User user);

    void signup(User user);

    void login(User user);

    boolean checkEmail(String targetEmail);
}
