package com.joezeo.joefgame.controller;

import com.joezeo.joefgame.dto.AccessTokenDTO;
import com.joezeo.joefgame.dto.GithubUser;
import com.joezeo.joefgame.dto.JsonResult;
import com.joezeo.joefgame.dto.UserDTO;
import com.joezeo.joefgame.pojo.User;
import com.joezeo.joefgame.provider.GithubProvider;
import com.joezeo.joefgame.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {

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

    @GetMapping("/login")
    public String htmLogin(){
        return "login";
    }

    @GetMapping("/signup")
    public String htmSignup(){
        return "signup";
    }

    /**
     * 进行github三方验证登录
     * @return
     */
    @GetMapping("/githubLogin")
    public String githubLogin(){
        String url = "https://github.com/login/oauth/authorize?client_id="+clientId+"&scope=user&state=1";
        return "redirect:" + url;
    }

    @PostMapping("login")
    @ResponseBody
    public JsonResult<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userService.login(user);

        Cookie cookie = new Cookie("token", token);
        if(userDTO.getRememberMe()){
            cookie.setMaxAge(60 * 60 * 24 * 7); // 存储7天登录信息
        }
        response.addCookie(cookie);

        return JsonResult.okOf(null);
    }

    /**
     * github
     * @param code github要求的接收参数
     * @param state 同上
     */
    @GetMapping("/callback")
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
            user.setGithubAccountId(githubUser.getId());
            user.setBio(githubUser.getBio());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());

            // 先进行检查数据库中是否已经有该条github用户数据，如果有则更新信息，没有则存入数据
            userService.createOrUpadate(user);

            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpSession session){
        // 移除cookie
        Cookie token = new Cookie("token", null);
        response.addCookie(token);
        token.setMaxAge(0);
        token.setPath("/");

        // 移除session
        session.removeAttribute("user");

        return "redirect:/";
    }

    @PostMapping("/signup")
    @ResponseBody
    public JsonResult signup(@RequestBody UserDTO userDTO, HttpServletResponse response){
        User user =new User();
        BeanUtils.copyProperties(userDTO, user);

        String token = UUID.randomUUID().toString();
        user.setToken(token);

        userService.signup(user);

        response.addCookie(new Cookie("token", token));
        return JsonResult.okOf(null);
    }

    @PostMapping("/authAccess")
    @ResponseBody
    public JsonResult authAccess(HttpServletResponse response){

        Cookie access = new Cookie("__access", UUID.randomUUID().toString());
        access.setMaxAge(60 * 60 * 24); // cookie储存一天
        response.addCookie(access);
        return JsonResult.okOf(null);
    }
}
