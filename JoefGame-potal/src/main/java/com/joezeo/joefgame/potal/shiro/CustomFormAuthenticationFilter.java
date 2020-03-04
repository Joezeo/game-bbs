package com.joezeo.joefgame.potal.shiro;

import com.joezeo.joefgame.potal.dto.UserDTO;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("ALL")
/**
 * 这个过滤器虽然配置了但是没有作用
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        UserDTO user = (UserDTO) token.getPrincipal();

        //获取session
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpSession session = httpServletRequest.getSession();

        // 将用户信息保存至session中
        session.setAttribute("user", user);
        return super.onLoginSuccess(token, subject, request, response);
    }

}
