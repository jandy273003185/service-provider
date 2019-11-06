package com.sevenpay.agentmanager.service;

import com.alibaba.dubbo.config.annotation.Reference;

import com.qifenqian.app.merchant.CommercialService;
import com.sevenpay.external.app.common.bean.TdCustInfo;
import com.sevenpay.external.app.common.bean.TdLoginUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CommerServiceImpl {

    @Reference
    CommercialService commercialService;



    /**
     * 查询商户审核信息
     * @param userId
     * @param custName
     * @param stateCode
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    public List<TdCustInfo> selectCommercialInfo(String userId,
                                                 String custName,
                                                 String stateCode,
                                                 String queryStartDate,
                                                 String queryEndDate){
        List<TdCustInfo> tdCustInfoList = commercialService.selectCommercialInfo(userId, custName, stateCode, queryStartDate, queryEndDate);

        return tdCustInfoList;
    }


    /**
     * 统计客户经理下的商户数据
     * @param userId
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    public Map<String,Object> getStatCommercial(String userId,
                                                String queryStartDate,
                                                String queryEndDate){
        return commercialService.getStatCommercial(userId, queryStartDate, queryEndDate);
    }


    /**
     * 交易排名
     * @param userId
     * @param rankingCode
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    public List<Map<String, Object>> getDealRanking(String userId,
                                                    String rankingCode,
                                                    String custName,
                                                    String queryStartDate,
                                                    String queryEndDate,
                                                    int pageSize,
                                                    int pageNum){


        return commercialService.getDealRanking(userId, rankingCode, custName,queryStartDate, queryEndDate,pageSize,pageNum);

    }


    /**
     * 获取商户审核失败原因
     * @param custId
     * @return
     */
    public List<Map<String,Object>> getCommerAuditCause(String custId){
        return commercialService.getCommerAuditCause(custId);
    }







}
