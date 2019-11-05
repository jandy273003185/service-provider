package com.example.customermanager.controller;

import com.example.customermanager.service.CommerServiceImpl;
import com.sevenpay.external.app.common.bean.Merchant;
import com.sevenpay.external.app.common.bean.TdCustInfo;


import com.sevenpay.external.app.common.util.GenSN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("comm")
public class CommercialController {

    @Autowired
    CommerServiceImpl commerService;


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
    public List<TdCustInfo> selectCommercialInfo(String userId,
                                                 String custName,
                                                 String stateCode,
                                                 String queryStartDate,
                                                 String queryEndDate){


        List<TdCustInfo> tdCustInfoList = commerService.selectCommercialInfo(userId, custName, stateCode, queryStartDate, queryEndDate);


        return tdCustInfoList;
    }

    /**
     * 统计客户经理下的商户数据
     * @param userId
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    @RequestMapping("getStatCommercial.do")
    public Map<String,Object> getStatCommercial(String userId,
                                                String queryStartDate,
                                                String queryEndDate){
        Map<String,Object> map = commerService.getStatCommercial(userId, queryStartDate, queryEndDate);
        System.out.println(map);

        return commerService.getStatCommercial(userId, queryStartDate, queryEndDate);
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
    public List<Map<String,Object>> getDealRanking(String userId,
                                                   String rankingCode,
                                                   String custName,
                                                   String queryStartDate,
                                                   String queryEndDate,
                                                   int pageSize,
                                                   int pageNum){


        return commerService.getDealRanking(userId, rankingCode,custName, queryStartDate, queryEndDate,pageSize,pageNum);
    }


    /**
     * 获取商户审核失败原因
     * @param custId
     * @return
     */
    @RequestMapping("getCommerAuditCause.do")
    public List<Map<String,Object>> getCommerAuditCause(String custId){
        return commerService.getCommerAuditCause(custId);
    }


    /**
     * 新增商户
     * @param merchant
     * @return
     */
    public String addMerchant(Merchant merchant){

        String custId = GenSN.getUUID();  //设置商户id

        return null;
    }




}
