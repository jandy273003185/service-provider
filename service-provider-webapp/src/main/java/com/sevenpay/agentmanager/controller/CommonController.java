package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.Bank;
import com.qifenqian.app.bean.TbBankProvincesInfoBean;
import com.qifenqian.app.bean.TbProvincesInfoBean;
import com.qifenqian.app.common.BankInfoService;
import com.qifenqian.app.customer.MerchantInfoService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResultBean("0");
    }

}
