package com.sevenpay.agentmanager.utils;

import com.alibaba.fastjson.JSONArray;
import com.qifenqian.app.bean.TdMerchantProductInfo;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 进件产品参数转换
 */
public class AddTdMerchantProductInfo {

    public static List<TdMerchantProductInfo> add(HttpServletRequest request,String custId) throws ParseException {
        return addTdMerchantProductInfo(request,custId);
    }

        private static List<TdMerchantProductInfo> addTdMerchantProductInfo(HttpServletRequest request,String custId) throws ParseException {
        //扫码
            String productInfos = request.getParameter("productInfos");
            List<TdMerchantProductInfo> merchantProductInfos = jsonToList(productInfos, TdMerchantProductInfo.class);//productId、productRate、sn
            for (TdMerchantProductInfo tdMerchantProductInfo : merchantProductInfos) {
                tdMerchantProductInfo.setMchCustId(custId);//商户编号
                tdMerchantProductInfo.setModifyTime(new Date());
                tdMerchantProductInfo.setCreateTime(new Date());//创建时间
                tdMerchantProductInfo.setProductStatus("99");//默认开通异常 99
            }
        return merchantProductInfos;
    }
    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }

    public static void main(String[] args) {
        String t = "[{productId:1,productRate:0.38},{productId:2,productRate:1.38}]";
        List<TdMerchantProductInfo> maps = jsonToList(t, TdMerchantProductInfo.class);
        int size = maps.size();
        System.out.println(size);

    }

}
