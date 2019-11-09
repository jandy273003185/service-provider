package com.sevenpay.agentmanager.utils;

import com.qifenqian.app.bean.TdMerchantProductInfo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTdMerchantProductInfo {

    public static List<TdMerchantProductInfo> add(HttpServletRequest request,String custId) throws ParseException {
        return addTdMerchantProductInfo(request,custId);
    }

        private static List<TdMerchantProductInfo> addTdMerchantProductInfo(HttpServletRequest request,String custId) throws ParseException {
        List<TdMerchantProductInfo> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        Date formatData = sdf.parse(format);
        TdMerchantProductInfo tdMerchantProductInfo = new TdMerchantProductInfo();
        tdMerchantProductInfo.setMchCustId(custId);//商户编号
        tdMerchantProductInfo.setCreateTime(formatData);//创建时间
        tdMerchantProductInfo.setProductStatus("99");//默认开通异常 99

        //扫码
        if ("1".equals(request.getParameter("scan"))) {
            tdMerchantProductInfo.setProductId(request.getParameter("scan"));//扫码标识  1
            int productRate = Integer.parseInt(request.getParameter("scanProductRate"));
            tdMerchantProductInfo.setProductRate(new BigDecimal(productRate));//扫码产品费率
            tdMerchantProductInfo.setSn(null);//设备号
            list.add(tdMerchantProductInfo);
        }
        //APP
        if ("2".equals(request.getParameter("app"))) {
            tdMerchantProductInfo.setProductId(request.getParameter("app"));//扫码标识  2
            int productRate = Integer.parseInt(request.getParameter("appProductRate"));
            tdMerchantProductInfo.setProductRate(new BigDecimal(productRate));//扫码产品费率
            tdMerchantProductInfo.setSn(null);//设备号
            list.add(tdMerchantProductInfo);
        }
        //蜻蜓
        if ("8".equals(request.getParameter("dragonfly"))) {
            int productRate = Integer.parseInt(request.getParameter("dragonflyProductRate"));//刷脸标识  8
            tdMerchantProductInfo.setProductRate(new BigDecimal(productRate));//扫码产品费率
            tdMerchantProductInfo.setSn("sn");//设备号
            list.add(tdMerchantProductInfo);
        }
        return list;
    }
}
