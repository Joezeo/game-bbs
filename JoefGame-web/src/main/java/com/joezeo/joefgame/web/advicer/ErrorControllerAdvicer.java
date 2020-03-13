package com.joezeo.joefgame.web.advicer;

import com.alibaba.fastjson.JSON;
import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class ErrorControllerAdvicer{

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType().toLowerCase();

        if("application/json;charset=utf-8".equals(contentType)){ // 如果以前后端分离的方式访问服务器
            JsonResult result;
            if(ex instanceof CustomizeException){
                result = JsonResult.errorOf((CustomizeException)ex);
            } else { // 处理包括 ServiceException 在内的异常情况
                ex.printStackTrace(); // 此处异常是系统异常，需打印异常信息排查错误
                result = JsonResult.errorOf(CustomizeErrorCode.SERVER_ERROR);
            }
            response.setContentType("application/json");
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            try (PrintWriter writer = response.getWriter()) {
                writer.write(JSON.toJSONString(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else { // 以get请求的方式访问服务器
            ModelAndView modelAndView = new ModelAndView("error");

            if(ex instanceof CustomizeException){
                modelAndView.addObject("message", ex.getMessage());
            } else { // 处理包括 ServiceException 在内的异常情况，不包括根目录下的异常
                ex.printStackTrace(); // 此处异常是系统异常，需打印异常信息排查错误
                modelAndView.addObject("message", "服务器冒烟啦，请稍后重试！");
            }

            return modelAndView;
        }
    }
}
