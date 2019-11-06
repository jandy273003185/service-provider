package com.sevenpay.agentmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.login.UserLoginManagerService;
import com.sevenpay.external.app.common.bean.SysUserInfo;
import com.sevenpay.external.app.common.bean.login.UserLoginRelate;
import org.gyzb.platform.common.utils.CipherUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("customer")
public class LoginController {

    @Reference
    private UserLoginManagerService loginManagerService;


    /**
     * 公众号服务商与其子账户登陆
     * @param openId 微信唯一标识
     * @param userCode 手机号
     * @param password 密码
     * @param roleId 管理员/业务员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "index",  method= RequestMethod.POST)
    public SysUserInfo indexHtml(String openId , String userCode, String password, String roleId) {


        Integer ifbing = loginManagerService.selectUserOpenid(openId);
        if(ifbing == 0){
            UserLoginRelate userLoginRelate = new UserLoginRelate();
            userLoginRelate.setUserId(userCode);
            userLoginRelate.setOpenId(openId);
            userLoginRelate.setLoginType("1");
            userLoginRelate.setUserType("cust");
            userLoginRelate.setIfUnbind("1");
            loginManagerService.userBinding(userLoginRelate);
        }

        if(StringUtils.isEmpty(userCode)) {
            throw new IllegalArgumentException("登陆手机号为空");
        }
        if(StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("登陆密码为空");
        }
        // 忽略大小写
        String queryUserCode = userCode.toUpperCase();
        // 查询用户
        SysUserInfo loginUser = loginManagerService.getSysUserInfo(queryUserCode);

        if(null == loginUser) {
            throw new IllegalArgumentException("员工号[" + userCode + "]不存在");
        }
        // 先校验密码，防止 别人根据提示套信息
        if (!loginUser.getPassword().equals(CipherUtils.encryptPassword(queryUserCode, password))) {
            throw new IllegalArgumentException("密码错误");
        }
        if(null == loginUser.getStatus()) {
            throw new IllegalArgumentException("用户[" + userCode + "]状态异常");
        }
        if(loginUser.getStatus().equals("冻结")) {
            throw new IllegalArgumentException("用户[" + userCode + "]已冻结");
        }
        if(loginUser.getStatus().equals("离职")) {
            throw new IllegalArgumentException("用户[" + userCode + "]已离职");
        }


        return loginUser;



    }





}
