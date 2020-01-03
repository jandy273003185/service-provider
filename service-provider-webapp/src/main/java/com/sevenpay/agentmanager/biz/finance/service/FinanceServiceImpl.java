package com.sevenpay.agentmanager.biz.finance.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.customer.FinanceInfo;
import com.qifenqian.app.enterprise.finance.FinanceManageService;
import com.sevenpay.agentmanager.core.bean.ResultData;
import com.sevenpay.agentmanager.core.exception.BizException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class FinanceServiceImpl {


    @Reference
    FinanceManageService financeManageService;


    /**
     * 添加财务员
     *
     * @param financeInfo
     * @return
     */
    public ResultData addFinance(FinanceInfo financeInfo) {

        /* 默认登陆密码 */
        financeInfo.setLoginPw("123456");

        /* 默认生效 */
        financeInfo.setStatus("1");

        /* 没有退款权限 */
        financeInfo.setRefundAuth("0");

        /* 查询权限 1-查询全部数据 */
        financeInfo.setQueryAuth("1");

        /* 创建时间 */
        financeInfo.setCreateTime(new Date());

        /* 创建人 */
        financeInfo.setCreateId(financeInfo.getCustId());

        try {
            boolean result = financeManageService.addFinance(financeInfo);
            if (result) {
                return ResultData.success();
            }
        } catch (Exception e) {
            throw new BizException("DUBBO接口异常" + e.getMessage());
        }

        return ResultData.error("添加出错");
    }



    /**
     * 财务员修改密码，退款密码
     * @param financeId
     * @param refundPw
     * @param refundNewPw
     * @param loginPw
     * @param loginNewPw
     * @return
     */
    public ResultData updateFinancePw(String financeId,
                                      String refundPw,
                                      String refundNewPw,
                                      String loginPw,
                                      String loginNewPw){

        try {
            boolean result = financeManageService.updateFinancePw(financeId, refundPw, refundNewPw, loginPw, loginNewPw);
            if (result) {
                return ResultData.success();
            }
        } catch (Exception e) {
            throw new BizException("DUBBO接口异常" + e.getMessage());
        }

        return ResultData.error("修改出错");
    }



    /**
     * 修改状态
     * @param financeInfo
     * @return
     */
    public ResultData updateFinance(FinanceInfo financeInfo){

        try {
            boolean result = financeManageService.updateFinance(financeInfo);
            if (result) {
                return ResultData.success();
            }
        } catch (Exception e) {
            throw new BizException("DUBBO接口异常" + e.getMessage());
        }
        return ResultData.error("修改出错");
    }



    /**
     * 财务员列表查询（手机或商户编号）
     * @param financeInfo
     * @return
     */
    public ResultData getFinanceList(FinanceInfo financeInfo){

        List<FinanceInfo> financeInfoList = financeManageService.getFinanceList(financeInfo);

        return ResultData.success(financeInfoList);
    }


    /**
     * 根据财务员编号查询收银员信息
     * @param financeId
     * @return
     */
    public FinanceInfo queryFinanceInfoByFinanceId(String financeId){

        return financeManageService.queryFinanceInfoByFinanceId(financeId);
    }


}
