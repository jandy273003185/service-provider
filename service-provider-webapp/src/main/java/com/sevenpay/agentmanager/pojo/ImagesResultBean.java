package com.sevenpay.agentmanager.pojo;

/**
 * 图片上传返回结果
 */
public class ImagesResultBean {

    //1成功 0失败
    private String status;

    //返回结果参数/path
    private String result;

    public ImagesResultBean(String status, String result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
