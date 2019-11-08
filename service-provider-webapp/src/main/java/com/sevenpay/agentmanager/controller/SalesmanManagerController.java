package com.sevenpay.agentmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.external.app.common.bean.customer.TdSalesmanInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("salesman")
public class SalesmanManagerController {

    @Reference
    private SalesmanManagerService salesmanManagerService;

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
    public List<TdSalesmanInfo> queryTdSalesmanInfos(TdSalesmanInfo salesmanInfo){
        List<TdSalesmanInfo> tdSalesmanInfos = salesmanManagerService.listTdSalesmanInfos(salesmanInfo);
        if (tdSalesmanInfos != null){//查询成功
            return tdSalesmanInfos;
        }
        return null;
    }

    /**
     * 根据主键id获取业务员详情
     * @param id 业务员编号
     * @return
     */
    @RequestMapping("get.do")
    public ResultBean<TdSalesmanInfo> getTdSalesmanInfoById(String id){
        TdSalesmanInfo tdSalesmanInfo = salesmanManagerService.getTdSalesmanInfoById(id);
        if (tdSalesmanInfo != null){//查询成功0
            return new ResultBean<>("1",tdSalesmanInfo);
        }
        //查询失败
        return new ResultBean<>("0",null);
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
}
