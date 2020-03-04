package com.joezeo.joefgame.potal.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class AuthPassListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // TODO: 验证码成功逻辑
    }
}
