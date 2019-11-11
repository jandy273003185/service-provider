package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qifenqian.app.bean.TdCustInfo;
import com.qifenqian.app.bean.TdCustScanCopy;
import com.qifenqian.app.bean.TdMerchantProductInfo;
import com.qifenqian.app.customer.MerchantInfoService;
import com.qifenqian.app.customer.MerchantStatusService;
import com.qifenqian.app.merchant.CommercialService;
import com.qifenqian.app.product.ProductInfoService;
import com.sevenpay.agentmanager.pojo.ImagesUri;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.AddCustScanCopy;
import com.sevenpay.agentmanager.utils.AddTdMerchantProductInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务商（管理员接口）
 */
@RestController
@RequestMapping("wx")
public class AgentController {

    @Reference
    private MerchantInfoService merchantInfoService;
    @Reference
    private CommercialService commerService;
    @Reference
    private ProductInfoService productInfoService;
    @Reference
    private MerchantStatusService merchantStatusService;

    @Value("${images.uri}")
    private String uri;
    @Value("${images.relativePath}")
    private String relativePath;


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
     * @param rankingCode transaction 笔数排名 money 金额排名
     * @param custName 商户名称
     * @param queryStartDate 查询起始时间
     * @param queryEndDate 查询终止时间
     * @param pageSize 页面条数
     * @param pageNum 当前页数
     * @return
     */
    @RequestMapping("getDealRanking.do")
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
            //进件完成
            return new ResultBean<>("1",custId);
        }
       return new ResultBean<>("0","商户进件失败");
    }

    /**
     * 查询未完善商户进件资料
     * @param tdCustInfo (custId)查询
     * @return
     */
    @RequestMapping("queryMerchant.do")
    public ResultBean queryMerchant(TdCustInfo tdCustInfo){
        Map<String,Object>map = new HashMap<>();
        //查询商户信息
        TdCustInfo custInfo = merchantInfoService.getCustInfo(tdCustInfo);
        if (custInfo != null) {
            map.put("mInfo",custInfo);
        }
        //查询商户签约产品
        TdMerchantProductInfo tdMerchantProductInfo = new TdMerchantProductInfo();
        tdMerchantProductInfo.setId(tdCustInfo.getCustId());
        List<TdMerchantProductInfo> merchantProductInfos = productInfoService.selectOpenProductInfo(tdMerchantProductInfo);
        if (merchantProductInfos != null) {
            map.put("pInfo",merchantProductInfos);
        }
        //查询商户扫描件信息，并将路径转化为URI的形式
        TdCustScanCopy tdCustScanCopy = new TdCustScanCopy();
        tdCustScanCopy.setCustId(tdCustInfo.getCustId());
        List<TdCustScanCopy> tdCustScanCopies = merchantInfoService.selectCustScanCopy(tdCustScanCopy);
        //存储回显图片uri
        ImagesUri Uris = new ImagesUri();
        for (TdCustScanCopy custScanCopy : tdCustScanCopies) {
            //获取扫描件路径
            String scanCopyPath = custScanCopy.getScanCopyPath();
            String imagesName = scanCopyPath.substring(scanCopyPath.lastIndexOf("/"));//
            StringBuilder imagesUri = new StringBuilder(uri).append(relativePath).append(imagesName);
            //00 个人身份证正面  01 税务登记证  02 营业执照 03 开户证件 04商户身份信息 05 银行卡扫描件 06 其他证件 18店内照  11行业资质照  12电子签名照
            //13 银行卡正面  14  银行卡反面  15合作证明函  16 个人身份证反面   18 店面内景   19 手持身份证正面   20 店面门头照   21 店面前台照  22 合作证明函
            if ("00".equals(custScanCopy.getCertifyType())){
                Uris.setIdentityCardFront(imagesUri.toString());
            }
            if ("16".equals(custScanCopy.getCertifyType())){
                Uris.setIdentityCardReverse(imagesUri.toString());
            }
            if ("02".equals(custScanCopy.getCertifyType())){
                Uris.setBusinessLicenseInOne(imagesUri.toString());
            }
            if ("03".equals(custScanCopy.getCertifyType())){
                Uris.setLicenceForOpeningAccounts(imagesUri.toString());
            }
            if ("13".equals(custScanCopy.getCertifyType())){
                Uris.setBankCardFront(imagesUri.toString());
            }
            if ("20".equals(custScanCopy.getCertifyType())){
                Uris.setShopFrontDoor(imagesUri.toString());
            }
            if ("18".equals(custScanCopy.getCertifyType())){
                Uris.setShopInterior(imagesUri.toString());
            }
            if ("12".equals(custScanCopy.getCertifyType())){
                Uris.setElectronicSignaturePhoto(imagesUri.toString());
            }
            if ("11".equals(custScanCopy.getCertifyType())){
                Uris.setSpecialBusiness(imagesUri.toString());
            }


        }
        if (tdCustScanCopies != null) {
            map.put("cInfo",tdCustScanCopies);
        }
        return new ResultBean("1",map);
    }


}
