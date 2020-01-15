package com.joezeo.community.controller;

import com.joezeo.community.dto.AccessTokenDTO;
import com.joezeo.community.dto.GithubUser;
import com.joezeo.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeCotroller {

    @Autowired
    private GithubProvider githubProvider;

    @RequestMapping("callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("332735b1b85bfbb88779");
        accessTokenDTO.setClient_secret("2ccaccd3819d864475d927bbbd3646f8ca6f2012");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);
        System.out.println(githubUser.getName() + "\r\n" + githubUser.getBio());
        return "index";
    }
}
