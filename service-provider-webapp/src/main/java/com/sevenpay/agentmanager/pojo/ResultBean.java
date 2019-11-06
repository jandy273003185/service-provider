package com.sevenpay.agentmanager.pojo;


/**
 * 返回结果
 */
public class ResultBean<T> {

    private String resultCode;
    private T resultMsg;

    public ResultBean(String resultCode, T resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public ResultBean(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public T getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(T resultMsg) {
        this.resultMsg = resultMsg;
    }
}

