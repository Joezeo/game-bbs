package com.joezeo.joefgame.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SpiderAccessControlIntercepter implements HandlerInterceptor {
    /**
     * 判断cookie：__access，用于防止爬虫策略
     * 如果没有这个cookie跳转至loadding页面，页面调用js函数获取cookie
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/loadding");
        } else {
            boolean hasAuth = false;
            for (Cookie cookie : cookies) {
                if ("__access".equals(cookie.getName())) {
                    if (cookie.getValue() != null && !"".equals(cookie.getValue())) {
                        hasAuth = true;
                        break;
                    }
                }
            }
            if (hasAuth) {
                return true;
            } else {
                response.sendRedirect("/loadding?uri=" + request.getRequestURI());
                return false;
            }
        }
        return true;
    }
}
