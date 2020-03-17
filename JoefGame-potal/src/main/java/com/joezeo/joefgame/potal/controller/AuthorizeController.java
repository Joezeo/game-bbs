package com.joezeo.joefgame.potal.controller;

import com.joezeo.joefgame.common.dto.GithubUser;
import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.SteamUser;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.utils.AuthUtils;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.potal.activiti.AuthPassListener;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
import java.util.List;
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
    public JsonResult<?> login(@RequestBody UserDTO userDTO,
                               HttpSession session) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        userService.login(user, userDTO.getRememberMe());

        userDTO = userService.queryUserByEmail(user.getEmail());
        session.setAttribute("user", userDTO);

        return JsonResult.okOf(null);
    }

    @PostMapping("/logout")
    public JsonResult<?> logout(HttpServletResponse response,
                                HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        userService.logout();

        return JsonResult.okOf(null);
    }

    @PostMapping("/signup")
    public JsonResult signup(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        // 验证验证码
        Task task = taskService.createTaskQuery().taskAssignee("system")
                .processVariableValueEquals("target", userDTO.getEmail()).singleResult();
        if (task == null) {
            // 验证时间超过5分钟，验证码失效
            return JsonResult.errorOf(CustomizeErrorCode.AUTHCODE_TIME_OUT);
        }

        String authcode = (String) runtimeService.getVariable(task.getExecutionId(), "authcode");

        if (authcode.equals(userDTO.getAuthCode())) { // 验证成功
            // 设置流程变量-密码、用户名，以便在Listener中使用
            runtimeService.setVariable(task.getExecutionId(), "password", userDTO.getPassword());
            runtimeService.setVariable(task.getExecutionId(), "name", userDTO.getName());
            if (userDTO.getGithubAccountId() != null) { // 说明是通过Github三方绑定注册
                runtimeService.setVariable(task.getExecutionId(), "githubID", userDTO.getGithubAccountId());
            }
            if (userDTO.getSteamId() != null) { // 说明是通过steam三方绑定注册
                runtimeService.setVariable(task.getExecutionId(), "steamID", userDTO.getSteamId());
            }
            taskService.complete(task.getId());
            // 验证码验证任务完成，执行监听器 AuthPassListener 进行注册操作
            return JsonResult.okOf(null);
        } else {
            return JsonResult.errorOf(CustomizeErrorCode.SIGNUP_WRONG_AUTHCODE);
        }
    }

    @PostMapping("getAuthcode")
    public JsonResult<?> getAuthcode(@RequestBody UserDTO userDTO) {
        String targetEmail = userDTO.getEmail();

        // 检查Email是否重复
        boolean isExist = userService.checkEmail(targetEmail);
        if (isExist) {
            return JsonResult.errorOf(CustomizeErrorCode.EMAIL_HAS_EXISTED);
        }

        // 检查是否存在同一个邮箱的注册流程
        List<ProcessInstance> processes = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("signup_auth_mail_process").list();
        for (ProcessInstance process : processes) {
            String email = (String) runtimeService.getVariable(process.getId(), "target");
            if (email.equals(targetEmail)) {
                runtimeService.deleteProcessInstance(process.getId(), "验证码未过有效期，用户重复获取验证码，流程重复");
                break;
            }
        }

        // 获取验证码
        String authCode = AuthUtils.generateAuthcode();

        // 设置流程变量
        Map<String, Object> varabies = new HashMap<>();
        varabies.put("target", targetEmail);
        varabies.put("authcode", authCode);
        varabies.put("date", TimeUtils.getCurrentDateStr());
        varabies.put("authPassListener", new AuthPassListener());
        // 启动流程
        runtimeService.startProcessInstanceByKey("signup_auth_mail_process", varabies);
        return JsonResult.okOf(null);
    }

    @PostMapping("/authAccess")
    public JsonResult authAccess(HttpServletResponse response) {
        Cookie access = new Cookie("__access", UUID.randomUUID().toString());
        access.setMaxAge(60 * 60 * 24); // cookie储存一天
        response.addCookie(access);
        return JsonResult.okOf(null);
    }

    @PostMapping("/getTmpGithubUser")
    JsonResult<GithubUser> getTmpGithubUser(HttpSession session) {
        GithubUser githubUser = (GithubUser) session.getAttribute("tempGithubUser");

        return JsonResult.okOf(githubUser);
    }

    @PostMapping("/getTmpSteamUser")
    JsonResult<GithubUser> getTmpSteamUser(HttpSession session) {
        SteamUser steamUser = (SteamUser) session.getAttribute("tempSteamUser");

        return JsonResult.okOf(steamUser);
    }
}
