package com.sevenpay.agentmanager.biz.agent.bean.domain;

import java.io.Serializable;

/**
 * ClassName：MerchantDO
 * Description：TODO
 * Author: LiBin
 * Date：2020/1/2 10:52 上午
 */
public class MerchantDO implements Serializable {
    private static final long serialVersionUID = 2639642722366749021L;

    /**
     * 商户编号
     */
    private String mchId;
    /**
     * 查询起始时间
     */
    private String queryStartDate;
    /**
     * 查询终止时间
     */
    private String queryEndDate;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(String queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public String getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }
}
