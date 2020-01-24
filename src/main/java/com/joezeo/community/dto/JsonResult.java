package com.joezeo.community.dto;

import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.IExceptionErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于前后端分离后进行数据传输对象
 * 后端向前端传递的json对象都是该对象
 *
 * 服务执行状态封装于字段success中
 * 服务执行后提示消息放置于message中
 * 服务执行后获得的数据封装于data中
 */
@Data
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = 5426736252370302612L;

    private Integer code;
    private Boolean success;
    private String message;
    private T data;

    private JsonResult() {
    }

    public static JsonResult errorOf(IExceptionErrorCode errorCode){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMessage(errorCode.getMessage());
        jsonResult.setCode(errorCode.getCode());
        jsonResult.setSuccess(false);
        return jsonResult;
    }

    public static JsonResult errorOf(CustomizeException ex) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(false);
        jsonResult.setMessage(ex.getMessage());
        jsonResult.setCode(ex.getCode());
        return jsonResult;
    }

    public static <T>JsonResult okOf(T data){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setMessage("OK");
        jsonResult.setData(data);
        return jsonResult;
    }

}
