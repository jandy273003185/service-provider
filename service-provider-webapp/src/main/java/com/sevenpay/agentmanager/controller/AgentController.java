package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qifenqian.app.bean.TdCustInfo;
import com.qifenqian.app.bean.TdCustScanCopy;
import com.qifenqian.app.bean.TdMerchantProductInfo;
import com.qifenqian.app.customer.MerchantInfoService;
import com.qifenqian.app.merchant.CommercialService;
import com.qifenqian.app.product.ProductInfoService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.AddCustScanCopy;
import com.sevenpay.agentmanager.utils.AddTdMerchantProductInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("wx")
public class AgentController {

    @Reference
    private MerchantInfoService merchantInfoService;
    @Reference
    private CommercialService commerService;
    @Reference
    private ProductInfoService productInfoService;
    /**
     * 查询商户审核信息
     * @param userId 管理员/业务员id
     * @param custName 商户名
     * @param stateCode 客户状态：00 有效；01 待审核；02 注销；03 冻结；04 审核不通过.
     * @param queryStartDate 起始时间
     * @param queryEndDate 结束时间
     * @return
     */
    @RequestMapping("selectCommercialInfo.do")
    public ResultBean selectCommercialInfo(String userId,
                                           String custName,
                                           String stateCode,
                                           String filingAuditStatus,
                                           String queryStartDate,
                                           String queryEndDate,
                                           int pageSize,
                                           int pageNum){
        PageInfo<TdCustInfo> tdCustInfoPageInfo = commerService.selectCommercialInfo(userId, custName, stateCode, filingAuditStatus, queryStartDate, queryEndDate, pageSize, pageNum);
        if (tdCustInfoPageInfo != null) {
            return new ResultBean("1",tdCustInfoPageInfo);
        }
        return new ResultBean("0");
    }

    /**
     * 统计服务商下的商户数据
     * @param userId 管理员/业务员id
     * @param queryStartDate 开始时间
     * @param queryEndDate 结束时间
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
     * @param userId 管理员
     * @param rankingCode
     * @param queryStartDate 查询起始时间
     * @param queryEndDate 查询终止时间
     * @return
     */
    @RequestMapping("getDealRanking")
    public ResultBean getDealRanking(String userId,
                                     String rankingCode,
                                     String custName,
                                     String queryStartDate,
                                     String queryEndDate,
                                     int pageSize,
                                     int pageNum){
        PageInfo<Map<String, Object>> transactionPage= commerService.getDealRanking(userId, rankingCode, custName, queryStartDate, queryEndDate, pageSize, pageNum);
        if (transactionPage != null) {
            return new ResultBean("1",transactionPage);
        }
        return new ResultBean("0");
    }


    /**
     * 获取商户审核失败原因
     * @param custId 商户编号
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
     * @param request
     * @param tdCustInfo
     * @return
     * @throws ParseException
     */
    @RequestMapping("insertMerchant.do")
    public ResultBean<String> addMerchant(HttpServletRequest request,
                                          TdCustInfo tdCustInfo) throws ParseException {
        Map<String, String> map = merchantInfoService.merchantAdd(tdCustInfo);
        String custId = map.get("custId");//商户编号
        if (custId != null) {
            //扫描件路径保存
            List<TdCustScanCopy> scanCopyList = AddCustScanCopy.add(request,custId);
            if (scanCopyList != null){
                for (TdCustScanCopy tdCustScanCopy : scanCopyList) {
                    merchantInfoService.saveTdCustScanCopy(tdCustScanCopy);
                }
            }
            //产品保存
            List<TdMerchantProductInfo> productList = AddTdMerchantProductInfo.add(request, custId);
            if (productList != null) {
                for (TdMerchantProductInfo tdMerchantProductInfo : productList) {
                    productInfoService.saveTdMerchantProductInfo(tdMerchantProductInfo);
                }
            }
            //进价完成
            return new ResultBean<>("1",custId);
        }
       return new ResultBean<>("0","商户进件失败");
    }

    /**
     * 查询未完善商户进件资料
     * @param request
     * @return
     */
    @RequestMapping("queryMerchant.do")
    public ResultBean queryMerchant(HttpServletRequest request){

        return new ResultBean("");
    }


}
