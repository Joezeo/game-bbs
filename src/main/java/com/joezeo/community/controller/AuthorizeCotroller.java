package com.joezeo.community.controller;

import com.joezeo.community.dto.AccessTokenDTO;
import com.joezeo.community.dto.GithubUser;
import com.joezeo.community.pojo.User;
import com.joezeo.community.provider.GithubProvider;
import com.joezeo.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeCotroller {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);
        if (githubUser != null) {
            User user = new User();
            // 生成token令牌，用于判断用户是否已登录
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(githubUser.getId());
            user.setBio(githubUser.getBio());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());

            // 先进行检查数据库中是否已经有该条github用户数据，如果有则更新信息，没有则存入数据
            User memUser = userService.queryByAccountid(user.getAccountId());
            if(memUser == null){ // Github第三方校验完成后，将用户信息存入数据库
            userService.addUser(user);
            } else { // 已经存在该用户，进行修改操作
                user.setId(memUser.getId());
                userService.updateUser(user);
            }

            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
}
