package com.joezeo.joefgame.potal.activiti;

import com.joezeo.joefgame.common.utils.SpringGetter;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.service.UserService;
import com.joezeo.joefgame.potal.service.impl.UserServiceImpl;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class AuthPassListener implements ExecutionListener {

    private static final long serialVersionUID = -8762771418466969185L;

    /**
     *  用户注册邮箱验证成功
     */
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // 进行注册账户操作
        String name = (String) execution.getVariable("name");
        String email = (String) execution.getVariable("target");
        String password = (String) execution.getVariable("password");
        String githubID = (String) execution.getVariable("githubID");
        String steamID = (String) execution.getVariable("steamID");

        UserService userService = SpringGetter.getBean(UserServiceImpl.class).get();
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        if (githubID != null) {
            user.setGithubAccountId(githubID);
        }
        if (steamID != null) {
            user.setSteamId(steamID);
        }
        userService.signup(user);
    }
}
