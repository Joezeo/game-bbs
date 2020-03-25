package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.*;
import com.joezeo.joefgame.potal.service.PostService;
import com.joezeo.joefgame.potal.service.SteamService;
import com.joezeo.joefgame.potal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private SteamService steamService;
    @Autowired
    private PostService postService;

    /**
     * 列举出用户关注的用户、Steam应用
     */
    @PostMapping("/listSubscribe")
    public JsonResult<PaginationDTO<?>> listSubscribe(@RequestBody PaginationDTO<?> paginationDTO, HttpSession session){
        UserDTO user = (UserDTO) session.getAttribute("user");
        PaginationDTO<?> pagination = null;
        if("user".equals(paginationDTO.getCondition())){
            pagination = userService.listFollowUser(user.getId(),paginationDTO.getPage());
        } else if("steam".equals(paginationDTO.getCondition())){
            pagination = userService.listFavoriteApp(user.getId(),paginationDTO.getPage());
        }
        return JsonResult.okOf(pagination);
    }

    @PostMapping("/getUserPosts")
    public JsonResult<List<UserPostDTO>> getUserPosts(HttpSession session){
        UserDTO user = (UserDTO) session.getAttribute("user");
        List<UserPostDTO> userPostDTOS = postService.getUserPosts(user.getId());
        return JsonResult.okOf(userPostDTOS);
    }

    @PostMapping("/getSteamNews")
    public JsonResult<List<SteamAppNew>> getSteamNews(HttpSession session){
        UserDTO user = (UserDTO) session.getAttribute("user");
        List<SteamAppNew> appNews = steamService.getAppNews(user.getOwnedGames());
        return JsonResult.okOf(appNews);
    }
}
