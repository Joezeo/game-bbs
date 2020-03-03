package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.utils.AuthUtils;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.activiti.AuthFailedListener;
import com.joezeo.joefgame.potal.activiti.AuthPassListener;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
@RestController
public class AuthorizeController {

    @Autowired
    private UserService userService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

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

    @PostMapping("/logout")
    @ResponseBody
    public JsonResult<?> logout(HttpServletResponse response,
                         HttpSession session){
        // 移除cookie
        Cookie token = new Cookie("token", null);
        response.addCookie(token);
        token.setMaxAge(0);
        token.setPath("/");

        // 移除session
        session.removeAttribute("user");

        return JsonResult.okOf(null);
    }

    @PostMapping("/signup")
    @ResponseBody
    public JsonResult signup(@RequestBody UserDTO userDTO, HttpServletResponse response){
        // 验证验证码
        Task task = taskService.createTaskQuery().taskAssignee("system")
                .processVariableValueEquals("target", userDTO.getEmail()).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        String authcode = (String) runtimeService.getVariable(processInstance.getId(), "authcode");
        if(authcode.equals(userDTO.getAuthCode())){ // 验证成功
            runtimeService.setVariable(processInstance.getId(), "flag", "true");
        } else { // 验证失败
            runtimeService.setVariable(processInstance.getId(), "flag", "false");
        }
        taskService.complete(task.getId());

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

    @PostMapping("getAuthcode")
    @ResponseBody
    public JsonResult<?> getAuthcode(@RequestBody UserDTO userDTO){
        String targetEmail = userDTO.getEmail();
        //TODO: 检查email是否重复
        boolean isExist = userService.checkEmail(targetEmail);
        if(isExist){
            return JsonResult.errorOf(CustomizeErrorCode.EMAIL_HAS_EXISTED);
        }

        // 获取验证码
        String authCode = AuthUtils.generateAuthcode();

        // 设置流程变量
        Map<String, Object> varabies = new HashMap<>();
        varabies.put("target", targetEmail);
        varabies.put("authcode", authCode);
        varabies.put("date", TimeUtils.getCurrentDateStr());
        varabies.put("authPassListener", new AuthPassListener());
        varabies.put("authFailedListener", new AuthFailedListener());

        // 启动流程
        runtimeService.startProcessInstanceByKey("signup_auth_mail_process", varabies);

        return JsonResult.okOf(null);
    }
}
