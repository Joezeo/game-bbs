package com.joezeo.joefgame.web.interceptor;

import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.NotificationService;
import com.joezeo.joefgame.potal.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session interceptor
 * 登录拦截
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
        if(userDTO == null){
            Subject subject = SecurityUtils.getSubject();
            UserDTO user = (UserDTO) subject.getPrincipal();
            request.getSession().setAttribute("user", user);
        }
        if(userDTO != null){
            // 加载该用户未阅读消息的数量
            int unreadCount = notificationService.countUnread(userDTO.getId());
            request.getSession().setAttribute("unreadCount", unreadCount);
        }
        return true;
    }
}
