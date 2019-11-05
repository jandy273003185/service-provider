package com.example.customermanager.controller;


import com.example.customermanager.service.LoginManagerServiceImpl;
import com.sevenpay.external.app.common.bean.SysUserInfo;
import com.sevenpay.external.app.common.bean.login.UserLoginRelate;
import org.gyzb.platform.common.utils.CipherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @Autowired
    LoginManagerServiceImpl loginManagerService;

    @ResponseBody
    @RequestMapping(value = "index",  method= RequestMethod.POST)
    public SysUserInfo indexHtml(String openId , String userCode, String password) {

        //1.获取openId 去wechat验证。
        //2.验证用户
        //3.进行绑定微信

        Integer ifbing = loginManagerService.selectUserOpenid(openId);
//        if(ifbing == 0){
//            UserLoginRelate userLoginRelate = new UserLoginRelate();
//            userLoginRelate.setUserId(userCode);
//            userLoginRelate.setOpenId(openId);
//            userLoginRelate.setLoginType("1");
//            userLoginRelate.setUserType("cust");
//            userLoginRelate.setIfUnbind("1");
//            loginManagerService.userBinding(userLoginRelate);
//        }

        if(StringUtils.isEmpty(userCode)) {
            throw new IllegalArgumentException("员工号为空");
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
