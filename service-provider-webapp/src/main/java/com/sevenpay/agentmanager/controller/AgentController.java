package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.merchant.CommercialService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.external.app.common.bean.TdCustInfo;
import com.sevenpay.external.app.common.bean.TdCustScanCopy;
import com.sevenpay.external.app.common.bean.TdMerchantProductInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("wx")
public class AgentController {

    @Reference
    private CommercialService commerService;


    /**
     * 查询商户审核信息
     * @param userId
     * @param custName
     * @param stateCode
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    @RequestMapping("selectCommercialInfo.do")
    public ResultBean<List<TdCustInfo>> selectCommercialInfo(String userId,
                                                 String custName,
                                                 String stateCode,
                                                 String queryStartDate,
                                                 String queryEndDate){
        List<TdCustInfo> tdCustInfoList = commerService.selectCommercialInfo(userId, custName, stateCode, queryStartDate, queryEndDate);
        if (tdCustInfoList != null) {
            return new ResultBean<>("1",tdCustInfoList);
        }
        return new ResultBean<>("0");
    }

    /**
     * 统计服务商下的商户数据
     * @param userId
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    @RequestMapping("getStatCommercial.do")
    public ResultBean<Map<String , Object>> getStatCommercial(String userId,
                                                String queryStartDate,
                                                String queryEndDate){
        Map<String,Object> map = commerService.getStatCommercial(userId, queryStartDate, queryEndDate);
        if (map != null) {
            return new ResultBean<>("1",map);
        }
        return new ResultBean<>("0");
    }


    /**
     * 交易排名
     * @param userId
     * @param rankingCode
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    @RequestMapping("getDealRanking")
    public ResultBean<List<Map<String , Object>>> getDealRanking(String userId,
                                                   String rankingCode,
                                                   String custName,
                                                   String queryStartDate,
                                                   String queryEndDate,
                                                   int pageSize,
                                                   int pageNum){
        List<Map<String, Object>> dealRanking = commerService.getDealRanking(userId, rankingCode, custName, queryStartDate, queryEndDate, pageSize, pageNum);
        if (dealRanking != null) {
            return new ResultBean<>("1",dealRanking);
        }
        return new ResultBean<>("0");
    }


    /**
     * 获取商户审核失败原因
     * @param custId
     * @return
     */
    @RequestMapping("getCommerAuditCause.do")
    public ResultBean<List<Map<String,Object>>>getCommerAuditCause(String custId){
        List<Map<String, Object>> commerAuditCause = commerService.getCommerAuditCause(custId);
        if (commerAuditCause != null) {
            return new ResultBean<>("1",commerAuditCause);
        }
        return new ResultBean<>("0");
    }


    /**
     * 新增商户(进件)
     * @return
     */
    @RequestMapping("insertMerchant.do")
    public ResultBean<String> addMerchant(String userId,
                                  String merchantAccount,
                                  TdCustInfo tdCustInfo,
                                  TdMerchantProductInfo tdMerchantProductInfo,
                                  TdCustScanCopy tdCustScanCopy){

        String jsonObjectString = commerService.merchantAdd(userId,merchantAccount,tdCustInfo,tdMerchantProductInfo,tdCustScanCopy);
        if (jsonObjectString != null) {
            return new ResultBean<>("1",jsonObjectString);
        }
        return new ResultBean<>("0",null);
    }

}
