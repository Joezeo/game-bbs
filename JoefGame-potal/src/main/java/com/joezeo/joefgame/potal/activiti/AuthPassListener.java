package com.joezeo.joefgame.potal.activiti;

import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.service.UserService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthPassListener implements ExecutionListener {

    @Autowired
    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // TODO: 验证码成功逻辑
        // 进行注册账户操作
        String name = (String) execution.getVariable("name");
        String email = (String) execution.getVariable("target");
        String password = (String) execution.getVariable("password");

        User user =new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        userService.signup(user);
    }
}
