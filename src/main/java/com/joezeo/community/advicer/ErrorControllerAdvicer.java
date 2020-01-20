package com.joezeo.community.advicer;

import com.joezeo.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvicer extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(Throwable ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error");
        if(ex instanceof CustomizeException){
            modelAndView.addObject("message", ex.getMessage());
        } else {
            modelAndView.addObject("message", "服务器冒烟啦，请稍后重试！");
        }

        return modelAndView;
    }
}
