package com.sevenpay.agentmanager.core.bean;

import java.io.Serializable;

/**
 * ClassName：BaseCondition
 * Description：TODO
 * Author: LiBin
 * Date：2020/1/2 1:53 下午
 */
public class BaseCondition implements Serializable {
    /*每页条数*/
    private int pageNo = 1;
    /*每页条数*/
    private int pageSize = 10;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
