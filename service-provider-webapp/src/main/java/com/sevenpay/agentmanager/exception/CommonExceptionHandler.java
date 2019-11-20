package com.sevenpay.agentmanager.exception;

import com.sevenpay.agentmanager.pojo.ResultBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 * User: JIANGZONGLIN
 * Date: 2019/11/11
 * Time: 21:36
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBean handleException(Exception e){
        return new ResultBean("10001","服务异常，请稍后在试！");
    }

}
