package com.example.customermanager.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.login.UserLoginManagerService;
import com.sevenpay.external.app.common.bean.SysUserInfo;
import com.sevenpay.external.app.common.bean.login.UserLoginRelate;
import org.springframework.stereotype.Service;


@Service
public class LoginManagerServiceImpl {


    @Reference
    UserLoginManagerService userLoginManagerService;


    /**
     * 用户查询
     * @param userCode
     * @return
     */
    public SysUserInfo getSysUserInfo(String userCode){


        SysUserInfo sInfo = userLoginManagerService.getSysUserInfo(userCode);
        return sInfo;
    }


    /**
     * 验证是否绑定过openid
     * @param openId
     * @return
     */
    public int selectUserOpenid(String openId){
        return userLoginManagerService.selectUserOpenid(openId);
    }


    /**
     * 用户绑定第三方
     * @param userLoginRelate
     * @return
     */
    public int userBinding(UserLoginRelate userLoginRelate){
        return userLoginManagerService.userBinding(userLoginRelate);
    }

}
