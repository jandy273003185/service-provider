package com.sevenpay.agentmanager.biz.agent.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.dto.trade.PayAndRefundBean;
import com.qifenqian.app.trade.PayOrderService;
import com.sevenpay.agentmanager.biz.agent.bean.domain.MerchantDO;
import com.sevenpay.agentmanager.core.bean.ResultData;
import com.sevenpay.agentmanager.core.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName：AgentServiceImpl
 * Description：TODO
 * Author: LiBin
 * Date：2020/1/2 10:30 上午
 */
@Service
public class AgentServiceImpl extends BaseService {


    @Reference
    private PayOrderService payOrderService;

    /** 
     * @description: 
     * @author: LiBin
     * @params [merchantDO]
     * @date: 2020-01-02 10:56:07
     */ 
    
    public ResultData agentService(MerchantDO merchantDO) {
        PayAndRefundBean payAndRefundBean = new PayAndRefundBean();
        BeanUtils.copyProperties(merchantDO, payAndRefundBean);
        List<PayAndRefundBean> payAndRefundBeans = payOrderService.getPayOrderAndRefundByBean(payAndRefundBean);
        return ResultData.success(payAndRefundBeans);
    }
}
