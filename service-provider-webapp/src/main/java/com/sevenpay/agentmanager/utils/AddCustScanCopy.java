package com.sevenpay.agentmanager.utils;


import com.qifenqian.app.bean.TdCustScanCopy;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddCustScanCopy{

    public static List<TdCustScanCopy> add(HttpServletRequest request,String merchantCode) throws ParseException {
        return addCustScanCopy(request,merchantCode);
    }

    private static List<TdCustScanCopy> addCustScanCopy(HttpServletRequest request,String merchantCode) throws ParseException {
        List<TdCustScanCopy> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        Date formatData = sdf.parse(format);
        //保存身份证正面
        TdCustScanCopy tdCustScanCopy = new TdCustScanCopy();
        tdCustScanCopy.setCustId(request.getParameter("custId"));//商户编号
        tdCustScanCopy.setCertifyType(request.getParameter("certifyType"));//扫描件类型
        tdCustScanCopy.setCertifyNo(request.getParameter("certifyNo"));//证件号码
        tdCustScanCopy.setScanCopyPath(request.getParameter("identityCardFront"));//证件路径
        tdCustScanCopy.setUploadTime(formatData);//上传申请时间
        tdCustScanCopy.setCreateTime(formatData);//创建时间
        tdCustScanCopy.setCreateId(request.getParameter("userId"));//创建人（服务商、业务员）
        tdCustScanCopy.setCertifyEndTime(request.getParameter("certifyEndTime"));//证件有效期
        tdCustScanCopy.setScanBelong("");//资料归属类型
        tdCustScanCopy.setStatus("01");//状态
        list.add(tdCustScanCopy);
        //保存身份证反面
        TdCustScanCopy tdCustScanCopy1 = tdCustScanCopy;
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
        tdCustScanCopy3.setScanCopyPath(request.getParameter("shopFrontDoor"));//证件路径
        list.add(tdCustScanCopy3);
        //保存店面内景照
        TdCustScanCopy tdCustScanCopy4 = tdCustScanCopy;
        tdCustScanCopy4.setScanCopyPath(request.getParameter("shopInterior"));//证件路径
        list.add(tdCustScanCopy4);
        if ("01".equals(request.getParameter("compMainAcctType"))){
            //营业执照（三合一）
        TdCustScanCopy tdCustScanCopy5 = tdCustScanCopy;
        tdCustScanCopy5.setScanCopyPath(request.getParameter("businessLicenseInOne"));//证件路径
        list.add(tdCustScanCopy5);
        //保存开户许可证
        TdCustScanCopy tdCustScanCopy6 = tdCustScanCopy;
        tdCustScanCopy6.setScanCopyPath(request.getParameter("licenceForOpeningAccounts"));//证件路径
        list.add(tdCustScanCopy6);
        }
        //保存电子签名照
        TdCustScanCopy tdCustScanCopy7 = tdCustScanCopy;
        tdCustScanCopy7.setScanCopyPath(request.getParameter("electronicSignaturePhoto"));//证件路径
        list.add(tdCustScanCopy7);
        //保存特殊行业资质照
        TdCustScanCopy tdCustScanCopy8 = tdCustScanCopy;
        tdCustScanCopy8.setScanCopyPath(request.getParameter("specialBusiness"));//证件路径
        list.add(tdCustScanCopy8);
        //其他资料照1
        TdCustScanCopy tdCustScanCopy9 = tdCustScanCopy;
        tdCustScanCopy9.setScanCopyPath(request.getParameter("orderPhoto1"));//证件路径
        list.add(tdCustScanCopy9);
        //其他资料照2
        TdCustScanCopy tdCustScanCopy10 = tdCustScanCopy;
        tdCustScanCopy10.setScanCopyPath(request.getParameter("orderPhoto2"));//证件路径
        list.add(tdCustScanCopy10);

        return list;
    }

}
