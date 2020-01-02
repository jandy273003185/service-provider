package com.sevenpay.agentmanager.core.bean;

import java.io.Serializable;

/**
 * ClassName：BaseCondition
 * Description：TODO
 * Author: LiBin
 * Date：2020/1/2 1:53 下午
 */
public class BaseCondition implements Serializable {
    private static final long serialVersionUID = 7018475839087842282L;
    /*每页条数*/
    private int pageNum = 1;
    /*每页条数*/
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
