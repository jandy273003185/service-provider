package com.sevenpay.agentmanager.core.controller.advice.exception;

import com.alibaba.fastjson.JSONObject;
import com.sevenpay.agentmanager.core.bean.ResultData;
import com.sevenpay.agentmanager.core.exception.BizException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName：ExceptionAdvice
 * Description：DO 统一处理异常
 * Author: LiBin
 * Date：2020/1/15 5:29 下午
 */
@ControllerAdvice
public class ExceptionAdvice {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 捕获所有异常，进行json封装返回
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public void resolveException(Exception ex, HttpServletResponse response) {
        // TODO 目前不进行异常甄别 所有异常都采取code msg data的形式返回
        response.setStatus(200);
        String code = "500";
        String msg = "请求异常!";
        if (ex instanceof BizException) {
            BizException bex = (BizException) ex;
            if (StringUtils.isNotBlank(bex.getCode())) {
                code = bex.getCode();
            }
            msg = ex.getMessage();
            logger.info("错误提示：" + ex.getMessage(), ex);
        } else if (ex instanceof AuthenticationException) {
            msg = ex.getCause().getMessage();
        } else {
            msg += ex.getMessage();
        }
        ResultData resultData = ResultData.error(code, msg);
        String responseStr = JSONObject.toJSONString(resultData);
        ajaxJson(responseStr, response);
    }

    /**
     * AJAX输出json，返回null
     **/
    public String ajaxJson(String json, HttpServletResponse response) {
        return ajax(json, "application/json", response);
    }

    /**
     * AJAX输出
     */
    public String ajax(String content, String type, HttpServletResponse response) {
        try {
            response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.error("ajax", e);
        }
        return null;
    }
}
