package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.GithubUser;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.dto.UserDTO;

public interface UserService {
    UserDTO queryUserByEmail(String email);

    UserDTO queryByAccountid(String accountId);

    void createOrUpadate(GithubUser githubUser);

    void signup(User user);

    void login(User user, boolean isRemember);

    boolean checkEmail(String targetEmail);

    void logout();
}
