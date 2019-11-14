package com.sevenpay.agentmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.customer.TdSalesmanInfo;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.qifenqian.app.merchant.CommercialService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 业务员接口
 */
@RestController
@RequestMapping("salesman")
public class SalesmanManagerController {

    @Reference
    private SalesmanManagerService salesmanManagerService;

    @Reference
    private CommercialService commerService;

    /**
     * 重置业务员密码
     * @param custId 服务商号
     * @param id 业务员号
     * @return
     */
    @RequestMapping("regPassword.do")
    public ResultBean regSalesman(String custId, String id){
        Integer result = salesmanManagerService.resetTdSalesmanInfoPassword(custId, id);
        if (result > 0) {
            return new ResultBean("1");
        }
        return new ResultBean("0");
    }

    /**
     * 业务员修改密码
     * @param id 业务员号
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @RequestMapping("updatePassword.do")
    public ResultBean updateSalesman(String id, String oldPassword, String newPassword){
        Integer result = salesmanManagerService.updateTdSalesmanInfoPassword(id,oldPassword,newPassword);
        if (result > 0) {
            return new ResultBean("1");
        }
        return new ResultBean("0");
    }

    /**
     * 查询业务员信息
     * @param salesmanInfo
     * @return
     */
    @RequestMapping("query.do")
    public ResultBean<List<TdSalesmanInfo>> queryTdSalesmanInfos(TdSalesmanInfo salesmanInfo){
        List<TdSalesmanInfo> tdSalesmanInfos = salesmanManagerService.listTdSalesmanInfos(salesmanInfo);
        return new ResultBean<>("1",tdSalesmanInfos);
    }

    /**
     * 根据主键id获取业务员业绩详情
     * @param id 业务员编号
     * @param queryStartDate 查询起始时间
     * @param queryEndDate 查询终止时间
     * @return
     */
    @RequestMapping("get.do")
    public ResultBean getTdSalesmanInfoById(String id,
                                            String queryStartDate,
                                            String queryEndDate){
        //查询商户数据
        Map<String, Object> statCommercial = commerService.getStatCommercial(id, queryStartDate, queryEndDate);
        //获取商户统计数，交易笔均金额
        Map<String, Object> merchantStatistics = salesmanManagerService.getMerchantStatistics(id, queryStartDate, queryEndDate);
        statCommercial.putAll(merchantStatistics);
        return new ResultBean<>("1",statCommercial);
    }

    /**
     * 新增业务员
     * @param tdSalesmanInfo
     * @return
     */
    @RequestMapping("insert.do")
    public ResultBean insertTdSalesmanInfoById(TdSalesmanInfo tdSalesmanInfo){
        Integer result = salesmanManagerService.insertTdSalesmanInfo(tdSalesmanInfo);
        if (result > 0) {//新增成功
            return new ResultBean("1");
        }
        //新增失败
        return new ResultBean("0");
    }

    /**
     * 更新业务员信息
     * @param tdSalesmanInfo
     * @return
     */
    @RequestMapping("update.do")
    public ResultBean updateTdSalesmanInfoById(TdSalesmanInfo tdSalesmanInfo){
        Integer result = salesmanManagerService.updateTdSalesmanInfo(tdSalesmanInfo);
        if (result > 0) {//更新成功
            return new ResultBean("1");
        }
        //更新失败
        return new ResultBean("0");
    }

    /**
     * 查询业务员业绩排名
     * @param sortType 0 按进件数  1 按交易额
     * @param custId 业务员所属服务商 userId
     * @param queryStartDate
     * @param queryEndDate
     * @return
     */
    @RequestMapping("performance.do")
    public ResultBean selectSalesmanPerformance(String sortType, String custId, String queryStartDate, String queryEndDate){
        List<Map<String, Object>> maps = salesmanManagerService.selectSalesmanPerformance(sortType, custId, queryStartDate, queryEndDate);
        return new ResultBean("1",maps);
    }


}
