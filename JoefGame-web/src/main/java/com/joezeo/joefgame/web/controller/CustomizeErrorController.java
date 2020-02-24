package com.joezeo.joefgame.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
