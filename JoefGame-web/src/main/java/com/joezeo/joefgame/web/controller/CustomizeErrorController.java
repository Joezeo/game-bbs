package com.joezeo.joefgame.web.controller;

import com.alibaba.fastjson.JSON;
import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 如果不定义此ErrorController
 * springmvc 会默认使用 BasicErrorController
 * 此异常处理器处理根路径下的异常
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        // 需判断前端的请求类型是不是application/json
        String contentType = request.getContentType();
        HttpStatus status = getStatus(request);

        if("application/json;charset=UTF-8".equals(contentType)) { // 如果以前后端分离的方式访问服务器
            JsonResult result;
            if (ex instanceof CustomizeException) {
                result = JsonResult.errorOf((CustomizeException) ex);
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
        }

        ModelAndView modelAndView = new ModelAndView("error");
        if (status.is4xxClientError()) {
            if (status.equals(HttpStatus.NOT_FOUND)) {
                modelAndView.addObject("message", "404：你来到了一片没有知识的荒野！");
            } else {
                modelAndView.addObject("message", "浏览器太累了，请稍后重试！");
            }
        } else if (status.is5xxServerError()) {
            modelAndView.addObject("message", "服务器冒烟了，请稍后重试！");
        }
        return modelAndView;
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
