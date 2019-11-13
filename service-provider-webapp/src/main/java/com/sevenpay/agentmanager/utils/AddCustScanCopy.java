package com.sevenpay.agentmanager.utils;


import com.qifenqian.app.bean.TdCustScanCopy;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 进件添加图片路径转换
 */
public class AddCustScanCopy{

    public static List<TdCustScanCopy> add(HttpServletRequest request,String custId) throws ParseException {
        return addCustScanCopy(request,custId);
    }

    private static List<TdCustScanCopy> addCustScanCopy(HttpServletRequest request,String custId) throws ParseException {
        List<TdCustScanCopy> list = new ArrayList<>();
        //保存身份证正面00 个人身份证正面  01 税务登记证  02 营业执照 03 开户证件 04商户身份信息 05 银行卡扫描件 06 其他证件 18店内照  11行业资质照  12电子签名照
        //	 * 	13 银行卡正面  14  银行卡反面  15合作证明函  16 个人身份证反面   18 店面内景   19 手持身份证正面   20 店面门头照   21 店面前台照  22 合作证明函
        TdCustScanCopy tdCustScanCopy = new TdCustScanCopy();
        tdCustScanCopy.setCustId(custId);//商户编号
        tdCustScanCopy.setCertifyType("00");//扫描件类型
        tdCustScanCopy.setCertifyNo(request.getParameter("certifyNo"));//证件号码
        tdCustScanCopy.setScanCopyPath(request.getParameter("identityCardFront"));//证件路径
        tdCustScanCopy.setCreateId(request.getParameter("userId"));//创建人（服务商、业务员）
        tdCustScanCopy.setCertifyEndTime(request.getParameter("certifyEndTime"));//证件有效期
        tdCustScanCopy.setScanBelong("00");//资料归属类型
        tdCustScanCopy.setStatus("01");//状态
        list.add(tdCustScanCopy);
        //保存身份证反面
        TdCustScanCopy tdCustScanCopy1 = tdCustScanCopy;
        tdCustScanCopy1.setCertifyType("16");//扫描件类型
        tdCustScanCopy1.setScanCopyPath(request.getParameter("identityCardReverse"));//证件路径
        list.add(tdCustScanCopy1);
        if ("02".equals(request.getParameter("compMainAcctType"))){
        //保存银行卡正面照
        TdCustScanCopy tdCustScanCopy2 = tdCustScanCopy;
        tdCustScanCopy2.setScanCopyPath(request.getParameter("bankCardFront"));//证件路径
        list.add(tdCustScanCopy2);
        }
        //保存店面门头照
        TdCustScanCopy tdCustScanCopy3 = tdCustScanCopy;
        tdCustScanCopy3.setCertifyType("20");//扫描件类型
        tdCustScanCopy3.setScanCopyPath(request.getParameter("shopFrontDoor"));//证件路径
        list.add(tdCustScanCopy3);
        //保存店面内景照
        TdCustScanCopy tdCustScanCopy4 = tdCustScanCopy;
        tdCustScanCopy4.setCertifyType("18");//扫描件类型
        tdCustScanCopy4.setScanCopyPath(request.getParameter("shopInterior"));//证件路径
        list.add(tdCustScanCopy4);
        if ("01".equals(request.getParameter("compMainAcctType"))){
            //营业执照（三合一）
            TdCustScanCopy tdCustScanCopy5 = tdCustScanCopy;
            tdCustScanCopy5.setCertifyType("02");//扫描件类型
            tdCustScanCopy5.setScanCopyPath(request.getParameter("businessLicenseInOne"));//证件路径
            list.add(tdCustScanCopy5);
            //保存开户许可证
            TdCustScanCopy tdCustScanCopy6 = tdCustScanCopy;
            tdCustScanCopy6.setCertifyType("03");//扫描件类型
            tdCustScanCopy6.setScanCopyPath(request.getParameter("licenceForOpeningAccounts"));//证件路径
            list.add(tdCustScanCopy6);
        }
        //保存电子签名照
        TdCustScanCopy tdCustScanCopy7 = tdCustScanCopy;
        tdCustScanCopy7.setCertifyType("12");//扫描件类型
        tdCustScanCopy7.setScanCopyPath(request.getParameter("electronicSignaturePhoto"));//证件路径
        list.add(tdCustScanCopy7);
        //保存特殊行业资质照
        TdCustScanCopy tdCustScanCopy8 = tdCustScanCopy;
        tdCustScanCopy8.setCertifyType("11");//扫描件类型
        tdCustScanCopy8.setScanCopyPath(request.getParameter("specialBusiness"));//证件路径
        list.add(tdCustScanCopy8);
        //其他资料照1
        TdCustScanCopy tdCustScanCopy9 = tdCustScanCopy;
        tdCustScanCopy9.setCertifyType("23");//扫描件类型
        tdCustScanCopy9.setScanCopyPath(request.getParameter("otherPhoto1"));//证件路径
        list.add(tdCustScanCopy9);
        //其他资料照2
        TdCustScanCopy tdCustScanCopy10 = tdCustScanCopy;
        tdCustScanCopy10.setCertifyType("24");//扫描件类型
        tdCustScanCopy10.setScanCopyPath(request.getParameter("otherPhoto2"));//证件路径
        list.add(tdCustScanCopy10);

        return list;
    }

}
