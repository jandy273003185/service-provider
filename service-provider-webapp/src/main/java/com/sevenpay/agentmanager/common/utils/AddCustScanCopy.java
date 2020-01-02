package com.sevenpay.agentmanager.common.utils;


import com.alibaba.fastjson.JSONArray;
import com.qifenqian.app.bean.TdCustScanCopy;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * 进件添加图片路径转换
 */
public class AddCustScanCopy{

    public static List<TdCustScanCopy> add(HttpServletRequest request,String custId) throws ParseException {
        return addCustScanCopy(request,custId);
    }

    private static List<TdCustScanCopy> addCustScanCopy(HttpServletRequest request,String custId) throws ParseException {
        //保存身份证正面00 个人身份证正面  01 税务登记证  02 营业执照 03 开户证件 04商户身份信息 05 银行卡扫描件 06 其他证件 18店内照  11行业资质照  12电子签名照
        //	 * 	13 银行卡正面  14  银行卡反面  15合作证明函  16 个人身份证反面   18 店面内景   19 手持身份证正面   20 店面门头照   21 店面前台照  22 合作证明函
        String custScanCopys = request.getParameter("custScanCopys");
        if (custScanCopys == null) {
            return null;
        }
        List<TdCustScanCopy> tdCustScanCopies = jsonToList(custScanCopys, TdCustScanCopy.class);//类型certifyType和路径scanCopyPath及证件号certifyNo
        for (TdCustScanCopy tdCustScanCopy : tdCustScanCopies) {
            tdCustScanCopy.setCustId(custId);//商户编号
            tdCustScanCopy.setCreateId(request.getParameter("userId"));//创建人（服务商、业务员）
            tdCustScanCopy.setCertifyEndTime(request.getParameter("certifyEndTime"));//证件有效期
            tdCustScanCopy.setStatus("00");//状态
        }
        return tdCustScanCopies;
    }
    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
}
