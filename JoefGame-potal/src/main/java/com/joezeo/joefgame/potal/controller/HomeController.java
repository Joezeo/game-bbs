package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

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
}
