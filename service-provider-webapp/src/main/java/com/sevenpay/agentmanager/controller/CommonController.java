package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.TbAreaInfoBean;
import com.qifenqian.app.bean.TbCityBean;
import com.qifenqian.app.bean.TbProvinceBean;
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

    /**
     * 省市县三级下拉
     * @param tbCityBean
     * @return
     */
    @RequestMapping("province.do")
    public ResultBean provinceQuery(TbAreaInfoBean tbAreaInfoBean){
        List<TbProvinceBean> provinceInfo = merchantInfoService.getProvinceInfo();
        if (tbAreaInfoBean.getProvinceId() != null){
            List<TbCityBean> cityInfo = merchantInfoService.getCityInfo(tbAreaInfoBean.getProvinceId());
            return new ResultBean("1",cityInfo);
        }

        if (tbAreaInfoBean != null){
            TbCityBean tbCityBean = new TbCityBean();
            tbCityBean.setCityId(tbAreaInfoBean.getCityId());
            tbCityBean.setProvinceId(tbAreaInfoBean.getProvinceId());
            List<TbAreaInfoBean> areaInfo = merchantInfoService.getAreaInfo(tbCityBean);
            return new ResultBean("1",areaInfo);
        }
        
        return new ResultBean("1",provinceInfo);
    }

}
