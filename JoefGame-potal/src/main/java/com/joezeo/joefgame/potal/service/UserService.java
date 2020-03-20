package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.UserDTO;

public interface UserService{
    UserDTO queryUserByEmail(String email);

    UserDTO queryByAccountid(String accountId);

    User queryByUserid(Long userid);

    void signup(User user);

    void loginBaseGithub(String githubID);

    void loginBaseSteam(String steamid);

    void login(User user, boolean isRemember);

    void logout();

    boolean checkEmail(String targetEmail);

    boolean isExistGithubUser(String githubID);

    boolean isExistSteamUser(String steamid);

    PaginationDTO<UserDTO> listFollowUser(Long userid, Integer page);

    PaginationDTO<SteamAppDTO> listFavoriteApp(Long userid, Integer page);
}
