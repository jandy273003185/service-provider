package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qifenqian.app.bean.*;
import com.qifenqian.app.customer.MerchantInfoService;
import com.qifenqian.app.customer.MerchantStatusService;
import com.qifenqian.app.merchant.CommercialService;
import com.qifenqian.app.product.ProductInfoService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.AddCustScanCopy;
import com.sevenpay.agentmanager.utils.AddTdMerchantProductInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务商（管理员接口）/产品接口
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
                                           int pageNum,
                                           String roleId){
        PageInfo<TdCustInfo> tdCustInfoPageInfo = commerService.selectCommercialInfo(userId, custName, stateCode, filingAuditStatus, queryStartDate, queryEndDate, pageSize, pageNum, roleId);
        return new ResultBean("1",tdCustInfoPageInfo);
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
                                                              String queryEndDate,
                                                              String roleId){
        Map<String,Object> map = commerService.getStatCommercial(userId, queryStartDate, queryEndDate,roleId);
        if (map != null) {
            return new ResultBean<>("1",map);
        }
        return new ResultBean<>("0");
    }

    /**
     * 交易排名
     * @param userId 管理员
     * @param custName 商户名称
     * @param queryStartDate 查询起始时间
     * @param queryEndDate 查询终止时间
     * @param pageSize 页面条数
     * @param roleId 2是管理员（服务商），3是业务员
     * @param pageNum 当前页数
     * @param rankingCode transactionNum desc      transactionNum asc
     * @return
     */
    @RequestMapping("getDealRanking.do")
    public ResultBean getDealRanking(String userId,
                                     String custName,
                                     String queryStartDate,
                                     String queryEndDate,
                                     String roleId,
                                     int pageSize,
                                     int pageNum,
                                     String rankingCode){
        if (roleId == null){
            return new ResultBean("0","roleId不能为空");
        }
        PageInfo<Map<String, Object>> transactionPage= commerService.getDealRanking(userId, custName, queryStartDate, queryEndDate, roleId, pageSize, pageNum,rankingCode);
        return new ResultBean("1",transactionPage);

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
     * 新增/修改商户(进件)
     * @param request
     * @param tdCustInfo
     * @return
     * @throws ParseException
     */
    @RequestMapping("insertMerchant.do")
    public ResultBean<String> addMerchant(HttpServletRequest request,
                                          TdCustInfo tdCustInfo) throws ParseException {

        TdCustInfo queryResult = merchantInfoService.getMerchantById(tdCustInfo.getCustId());

        if (queryResult == null){//保存到数据库待审核/待完善
            tdCustInfo.setCreateTime(new Date());
            tdCustInfo.setCreateId(request.getParameter("userId"));
            tdCustInfo.setMerchantMobile(tdCustInfo.getMerchantAccount());
            Map<String, Object> map = merchantInfoService.merchantAdd(tdCustInfo);
            String custId = (String) map.get("custId");//商户编号
            if (custId != null) {
                //扫描件路径保存
                List<TdCustScanCopy> scanCopyList = AddCustScanCopy.add(request, custId);
                if (scanCopyList != null) {
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
                return new ResultBean("1", custId);
            }
            return new ResultBean("0",map);
        }else {//完善后提交（修改操作）
            tdCustInfo.setMerchantMobile(tdCustInfo.getMerchantAccount());
            String custId1 = tdCustInfo.getCustId();//商户编号
            tdCustInfo.setModifyTime(new Date());//修改时间
            tdCustInfo.setModifyId(request.getParameter("userId"));//修改人
            if (custId1 != null) {
                merchantInfoService.updateMerchant(tdCustInfo);
                //扫描件路径保存
                List<TdCustScanCopy> scanCopyList = AddCustScanCopy.add(request,custId1);
                if (scanCopyList.size() > 0){
                    for (TdCustScanCopy tdCustScanCopy : scanCopyList) {
                        merchantInfoService.updateTdCustScanCopy(tdCustScanCopy);
                    }
                }
                //产品保存
                TdMerchantProductInfo MerchantProductInfo = new TdMerchantProductInfo();
                MerchantProductInfo.setMchCustId(custId1);
                List<TdMerchantProductInfo> productList = AddTdMerchantProductInfo.add(request, custId1);
                List<TdMerchantProductInfo> merchantProductInfos = productInfoService.selectOpenProductInfo(MerchantProductInfo);
                for (TdMerchantProductInfo merchantProductInfo : merchantProductInfos) {//删除旧的产品
                    productInfoService.delMerchantProduct(merchantProductInfo);
                }
                for (TdMerchantProductInfo tdMerchantProductInfo : productList) {//存储新的产品
                    productInfoService.selectOpenProductInfo(tdMerchantProductInfo);
                }
                //修改完成
                return new ResultBean("1",custId1);
            }
        }
       return new ResultBean("0","商户进件失败");
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
        map.put("custInfo",custInfo);
        //查询省市区
        TbProvincesInfoBean tbProvincesInfoBean = new TbProvincesInfoBean(custInfo.getProvince(),custInfo.getCity(),custInfo.getCountry());
        List<TbProvincesInfoBean> provinces = merchantInfoService.getProvinces(tbProvincesInfoBean);
        map.put("provinces",provinces);
        //查询银行省市
        TbBankProvincesInfoBean tbBankProvincesInfoBean = new TbBankProvincesInfoBean();
        tbBankProvincesInfoBean.setBankCityId(custInfo.getBankCityName());
        tbBankProvincesInfoBean.setBankProvinceId(custInfo.getBankProvinceName());
        List<TbBankProvincesInfoBean> bankProvinces = merchantInfoService.getBankProvinces(tbBankProvincesInfoBean);
        map.put("bankProvinces",bankProvinces);
        //查询商户签约产品
        TdMerchantProductInfo tdMerchantProductInfo = new TdMerchantProductInfo();
        tdMerchantProductInfo.setMchCustId(tdCustInfo.getCustId());
        List<TdMerchantProductInfo> merchantProductInfos = productInfoService.selectOpenProductInfo(tdMerchantProductInfo);
        map.put("productInfoList",merchantProductInfos);
        //查询商户扫描件信息，并将路径转化为URI的形式
        TdCustScanCopy tdCustScanCopy = new TdCustScanCopy();
        tdCustScanCopy.setCustId(tdCustInfo.getCustId());
        List<TdCustScanCopy> tdCustScanCopies = merchantInfoService.selectCustScanCopy(tdCustScanCopy);
        map.put("uri",uri);
        map.put("url",relativePath);
        map.put("custScanInfoList",tdCustScanCopies);
        return new ResultBean("1",map);
    }

    /**
     * 添加产品
     * @param request
     * @param custId
     * @return
     * @throws ParseException
     */
    @RequestMapping("insertProduct.do")
    public ResultBean insertProduct(HttpServletRequest request,String custId) throws ParseException, IOException {
        List<TdMerchantProductInfo> productInfos = AddTdMerchantProductInfo.add(request, custId);
        if (productInfos.size() > 0){
            for (TdMerchantProductInfo productInfo : productInfos) {
                productInfoService.saveTdMerchantProductInfo(productInfo);
            }
            return new ResultBean("1","签约产品提交成功,待审核");
        }
        return new ResultBean("0","签约产品提交失败");
    }

    @RequestMapping("delProduct.do")
    public ResultBean delProduct(TdMerchantProductInfo tdMerchantProductInfo){

        return new ResultBean("");
    }

    /**
     * 查询商户已签约产品
     * @param tdMerchantProductInfo
     * @return
     */
    @RequestMapping("queryProduct.do")
    public ResultBean queryProduct(TdMerchantProductInfo tdMerchantProductInfo) {
        List<TdMerchantProductInfo> merchantProductInfos = productInfoService.selectOpenProductInfo(tdMerchantProductInfo);
        return new ResultBean("1",merchantProductInfos);
    }

    /**
     * 查询商户信息
     * @param custId
     * @return
     */
    @RequestMapping("queryMerchantById.do")
    public ResultBean queryProduct(String custId) {
        TdCustInfo custInfo = merchantInfoService.getMerchantById(custId);
        return new ResultBean("1",custInfo);
    }

    /**
     * 设备号校验
     * @param sn 设备号
     * @return
     */
    @RequestMapping("checkSn.do")
    public ResultBean checkSn(String sn){
        boolean result = commerService.isPertainToAgent(sn);
        if (result){
            return new ResultBean("1","校验通过");
        }
        return new ResultBean("0","校验失败");
    }




}
