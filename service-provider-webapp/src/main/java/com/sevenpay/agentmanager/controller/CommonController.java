package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.Bank;
import com.qifenqian.app.bean.TbBankProvincesInfoBean;
import com.qifenqian.app.bean.TbProvincesInfoBean;
import com.qifenqian.app.bean.dto.MessageDTO;
import com.qifenqian.app.common.BankInfoService;
import com.qifenqian.app.common.MessageManager;
import com.qifenqian.app.customer.MerchantInfoService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.GenSN;
import com.sevenpay.agentmanager.utils.verfycode.ToolsUtil;
import com.sevenpay.agentmanager.utils.verfycode.VerifyInfoConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * User: JIANGZONGLIN
 * Date: 2019/11/14
 * Time: 19:59
 */
@RestController
@RequestMapping("common")
public class CommonController {

    @Reference
    private MerchantInfoService merchantInfoService;

    @Reference
    private BankInfoService bankInfoService;

    @Reference
    private MessageManager messageManager;


    /**
     * 省市县三级下拉
     * @param tbProvincesInfoBean
     * @return
     */
    @RequestMapping("province")
    public ResultBean<?> provinceQuery(TbProvincesInfoBean tbProvincesInfoBean){
        List<TbProvincesInfoBean> provinces = merchantInfoService.getProvinces(tbProvincesInfoBean);
        return new ResultBean<List<TbProvincesInfoBean>>("1",provinces);
    }

    /**
     * 银行省市二级下拉
     * @param tbBankProvincesInfoBean
     * @return
     */
    @RequestMapping("bankProvince")
    public ResultBean<?> provinceQuery(TbBankProvincesInfoBean tbBankProvincesInfoBean){
        List<TbBankProvincesInfoBean> bankProvinces = merchantInfoService.getBankProvinces(tbBankProvincesInfoBean);
        return new ResultBean<List<TbBankProvincesInfoBean>>("1",bankProvinces);
    }

    /**
     * 查询总行（带模糊）
     * @param bank
     * @return
     */
    @RequestMapping("bankHeadOffice")
    public ResultBean bankHeadOffice(Bank bank){
        List<Bank> banks = bankInfoService.selectBanks(bank);
        if (banks.size() > 0) {
            return new ResultBean("1",banks);
        }
        return new ResultBean("0");
    }

    /**
     * 查询支行（带模糊）
     * @param bank
     * @return
     */
    @RequestMapping("bankBranch")
    public ResultBean bankBranch(Bank bank){
        List<Bank> banks = bankInfoService.selectBranchBanks(bank);
        if (banks.size() > 0) {
            return new ResultBean("1",banks);
        }
        Bank bankHeadOffice = bankInfoService.selectBankByBankCode(bank.getBankCode());
        bankHeadOffice.setBranchBankCode(bankHeadOffice.getBankCode());
        bankHeadOffice.setBranchBankName(bankHeadOffice.getBankName());
        banks.add(bankHeadOffice);
        return new ResultBean("1",banks);
    }

    /**
     * 发送短信验证码接口
     * @param request
     * @param messageDTO
     * @return
     */
    @RequestMapping("verifyCode")
    public ResultBean verifyCode(HttpServletRequest request, MessageDTO messageDTO){
        Date sendTime = (Date)request.getSession().getAttribute("REGISTER_VERIFY_SEND_TIME");

        //短信发送间隔必须超过55s
        if(null != sendTime && ToolsUtil.calTimeSec(new Date(), sendTime) < 55)
        {
            return new ResultBean("0","验证码发送间隔为60s");
        }

        //生成验证码
        String smsVerifyCode = GenSN.getRandomNum(6);
        request.getSession().setAttribute(VerifyInfoConstant.REGISTER_VERIFY_CODE, smsVerifyCode);
        request.getSession().setAttribute(VerifyInfoConstant.REGISTER_VERIFY_ACC, messageDTO.getAddressee());
        request.getSession().setAttribute(VerifyInfoConstant.REGISTER_VERIFY_SEND_TIME, new Date());
        messageDTO.setCode(smsVerifyCode);
        MessageDTO m = messageManager.sendMobileCode(messageDTO);
        return new ResultBean("1");
    }


}
