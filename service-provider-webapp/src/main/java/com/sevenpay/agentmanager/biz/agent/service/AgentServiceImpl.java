package com.sevenpay.agentmanager.biz.agent.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.dto.TdOrderRateDetailProfitDTO;
import com.qifenqian.app.bean.dto.trade.PayAndRefundBean;
import com.qifenqian.app.bean.dto.trade.TdOrderRateDetailCondition;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.qifenqian.app.merchant.CommercialService;
import com.qifenqian.app.trade.PayOrderService;
import com.sevenpay.agentmanager.biz.agent.bean.condition.MerchantCondition;
import com.sevenpay.agentmanager.core.bean.ResultData;
import com.sevenpay.agentmanager.core.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ClassName：AgentServiceImpl
 * Description：TODO
 * Author: LiBin
 * Date：2020/1/2 10:30 上午
 */
@Service
public class AgentServiceImpl extends BaseService {
    @Reference
    private SalesmanManagerService salesmanManagerService;
    @Reference
    private CommercialService commerService;
    @Reference
    private PayOrderService payOrderService;

    /**
     * @description: 查询商户支付信息
     * @author: LiBin
     * @params [merchantDO]
     * @date: 2020-01-02 10:56:07
     */

    public ResultData getSPMerchantOrderList(MerchantCondition merchantCondition) {
        PayAndRefundBean payAndRefundBean = new PayAndRefundBean();
        BeanUtils.copyProperties(merchantCondition, payAndRefundBean);
        List<PayAndRefundBean> payAndRefundBeans = payOrderService.getPayOrderAndRefundByBean(payAndRefundBean);
        return ResultData.success(payAndRefundBeans);
    }

    /**
     * @description: 针对各个门店的交易记录, 发送邮件
     * @author: LiBin
     * @params [userId, custName, queryStartDate, queryEndDate, roleId, rankingCode]
     * @date: 2020-01-03 17:32:22
     */


    public ResultData sentEmailByDealRanking(String userId, String custName, String queryStartDate, String queryEndDate, String roleId, String rankingCode) {
        int pageNum = 1;
        int pageSize = Integer.MAX_VALUE;
        List<Map<String, Object>> list = commerService.getDealRanking(userId, custName, queryStartDate, queryEndDate, roleId, pageSize, pageNum, rankingCode);
        for (Map<String, Object> stringObjectMap : list) {
            stringObjectMap.put("queryStartDate", queryStartDate);
            stringObjectMap.put("queryEndDate", queryEndDate);
            String custId = (String) stringObjectMap.get("CUST_ID");
            /**
             * TODO 调用分润接口
             * 根据custId 取出具体的值丢在map
             */
        }
        /**
         * TODO 调用发送邮件,发送数据为list
         */
        return ResultData.success();
    }

    public ResultData getShareProfit(TdOrderRateDetailCondition tdOrderRateDetailCondition) {
        TdOrderRateDetailProfitDTO tdOrderRateDetailProfitDTO = payOrderService.getShareProfit(tdOrderRateDetailCondition);
        if (null == tdOrderRateDetailProfitDTO) {
            tdOrderRateDetailProfitDTO = new TdOrderRateDetailProfitDTO();
        }
        return ResultData.success(tdOrderRateDetailProfitDTO);
    }
}
