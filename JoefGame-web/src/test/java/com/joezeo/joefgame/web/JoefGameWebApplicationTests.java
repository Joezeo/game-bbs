package com.joezeo.joefgame.web;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SuppressWarnings("ALL")
@SpringBootTest
class JoefGameWebApplicationTests {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void deleteTask() {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().processDefinitionKey("signup_auth_mail_process").list();

        for (ProcessInstance instance : instances) {
            runtimeService.deleteProcessInstance(instance.getId(), "测试");
        }
    }

}
