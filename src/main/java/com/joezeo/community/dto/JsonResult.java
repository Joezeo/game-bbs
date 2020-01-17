package com.joezeo.community.dto;

import java.io.Serializable;

/**
 * 用于前后端分离后进行数据传输对象
 * 后端向前端传递的json对象都是该对象
 *
 * 服务执行状态封装于字段success中
 * 服务执行后提示消息放置于message中
 * 服务执行后获得的数据封装于data中
 */
public class JsonResult implements Serializable {
    private static final long serialVersionUID = 5426736252370302612L;

    private Boolean success;
    private String message;
    private Object data;

    public JsonResult() {
    }

    public JsonResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public JsonResult(Object data) {
        this.success = true;
        this.message = "OK";
        this.data = data;
    }

    public JsonResult(Exception e){
        this.success = false;
        this.message = e.getMessage();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
