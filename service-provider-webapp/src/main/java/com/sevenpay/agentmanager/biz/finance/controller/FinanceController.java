package com.sevenpay.agentmanager.biz.finance.controller;


import com.qifenqian.app.bean.customer.FinanceInfo;
import com.sevenpay.agentmanager.biz.finance.service.FinanceServiceImpl;
import com.sevenpay.agentmanager.core.bean.ResultData;
import com.sevenpay.agentmanager.core.controller.AbstractBaseController;
import com.sevenpay.agentmanager.core.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务员
 */
@RestController
@RequestMapping("finance")
public class FinanceController extends AbstractBaseController {


    @Autowired
    FinanceServiceImpl financeService;



    /**
     * 服务商添加财务员
     * @param financeInfo
     * @return
     */
    @RequestMapping(value = "addFinance")
    public ResultData addFinance(FinanceInfo financeInfo){

        if(null == financeInfo.getFinanceMobile()){
            throw new BizException("手机不能为空~");
        }
        if(null == financeInfo.getFinanceName()){
            throw new BizException("姓名不能为空~");
        }
        if(null == financeInfo.getCustId()){
            throw new BizException("服务商id不能为空~");
        }

        return financeService.addFinance(financeInfo);

    }


    /**
     * 财务员列表查询（手机或商户编号）
     * @param custId
     * @return
     */
    @RequestMapping(value = "getFinanceList")
    public ResultData getFinanceList(String custId){

        if(custId.equals("")||null == custId){
            throw new BizException("服务商id不能为空~");
        }

        FinanceInfo financeInfo = new FinanceInfo();


        return financeService.getFinanceList(financeInfo);
    }



    /**
     * 财务员修改密码、退款密码
     * @param financeId
     * @param refundPw
     * @param refundNewPw
     * @param loginPw
     * @param loginNewPw
     * @return
     */
    @RequestMapping(value = "updateFinancePw")
    public ResultData updateFinancePw(String financeId, String refundPw, String refundNewPw, String loginPw, String loginNewPw){
        if(null == financeId){
            throw new BizException("财务员id不能为空~");
        }
        if(null == loginPw){
            throw new BizException("原密码不能为空~");
        }
        if(null == loginNewPw){
            throw new BizException("新密码不能为空~");
        }


        return financeService.updateFinancePw(financeId,refundPw,refundNewPw,loginPw,loginNewPw);

    }


    /**
     * 重置密码
     * @param financeInfo
     * @return
     */
    @RequestMapping(value = "resetFinancePw")
    public ResultData resetFinancePw(FinanceInfo financeInfo){
        if(null == financeInfo.getFinanceId()){
            throw new BizException("财务员id不能为空~");
        }
        financeInfo.setLoginPw("123456");
        return  financeService.updateFinance(financeInfo);
    }






    /**
     * 修改状态
     * @param financeInfo
     * @return
     */
    @RequestMapping(value = "updateFinance")
    public ResultData updateFinance(FinanceInfo financeInfo){

        return financeService.updateFinance(financeInfo);

    }





}
