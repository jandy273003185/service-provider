package com.sevenpay.agentmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.UserLoginRelate;
import com.qifenqian.app.bean.customer.TdSalesmanInfo;
import com.qifenqian.app.bean.dto.UserDTO;
import com.qifenqian.app.customer.MerchantStatusService;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.qifenqian.app.login.UserLoginManagerService;
import com.qifenqian.app.user.UserManager;
import com.sevenpay.agentmanager.jwt.JWTUtil;
import com.sevenpay.agentmanager.pojo.LoginUser;
import com.sevenpay.agentmanager.pojo.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 服务商/业务员
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Reference
    private UserLoginManagerService loginManagerService;
    @Reference
    private UserManager userManager;
    @Reference
    private SalesmanManagerService salesmanManagerService;
    @Reference
    private MerchantStatusService merchantStatusService;
    /**
     * 登陆
     * @param userName 账号
     * @param password 密码
     * @param openId 微信用户唯一标识
     * @param roleCode 用户角色agent管理员（服务商）/salesman业务员
     * @return
     */
    @RequestMapping("/loginBinding")
    public ResultBean loginBinding(String userName, String password, String openId, String roleCode){

        //不同的角色获取不同的用户信息表
        LoginUser loginUser=new LoginUser();

        //登陆校验
        if ("agent".equals(roleCode)) {//服务商绑定
            UserDTO userInfo = userManager.login(userName, password, roleCode);
            if(!userInfo.isSuccess()){
                return new ResultBean("0","账号或密码错误");
            }
            UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId
            if (ifbing != null){
                return new ResultBean("0","该微信已经与七分钱账号绑定");
            }

            UserLoginRelate userLoginRelate = new UserLoginRelate();
            userLoginRelate.setUserId(userInfo.getCustId());
            userLoginRelate.setOpenId(openId);
            userLoginRelate.setLoginType("1");
            userLoginRelate.setUserType(roleCode);
            userLoginRelate.setIfUnbind("1");
            loginManagerService.userBinding(userLoginRelate);//用户绑定openId
            loginUser.setUserInfo(userInfo.getCustId());
            //根据用户编号和密码加密生成token
            String token = JWTUtil.sign(userInfo.getCustId(),openId);
            loginUser.setToken(token);
            return new ResultBean<>("1",loginUser) ;//
        }
        if ("salesman".equals(roleCode)){
            TdSalesmanInfo userInfo = salesmanManagerService.checkSalesmanLogin(userName, password);
            if(StringUtils.isEmpty(userInfo)){
                return new ResultBean("0","账号或密码错误");
            }
            UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId
            if (ifbing != null){
                return new ResultBean("0","该微信已经与七分钱账号绑定");
            }else if (!"agent".equals(ifbing.getUserType())){
                return new ResultBean("0","该账号不是业务员账号");
            }
            UserLoginRelate userLoginRelate = new UserLoginRelate();
            userLoginRelate.setUserId(String.valueOf(userInfo.getId()));
            userLoginRelate.setOpenId(openId);
            userLoginRelate.setLoginType("1");
            userLoginRelate.setUserType(roleCode);
            userLoginRelate.setIfUnbind("1");
            loginManagerService.userBinding(userLoginRelate);//用户绑定openId
            loginUser.setUserId(userInfo.getCustId());
            //根据用户编号和密码加密生成token
            String token = JWTUtil.sign(userInfo.getCustId(),openId);
            loginUser.setToken(token);
            loginUser.setPhoneCode(userInfo.getUserPhone());
            return new ResultBean<>("1",loginUser) ;
        }
        return new ResultBean("0","绑定失败,请重新登陆");
    }


    /**
     * 第一步登陆
     * @param openId
     * @return
     */
    @RequestMapping("/login")
    public ResultBean login(String openId,String roleId){

        UserLoginRelate ifbing = loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId

        if(ifbing == null){
            return new ResultBean("0",openId);  //返回页面去登陆 进行绑定
        }else {
            String userId = ifbing.getUserId();//获取角色
            //不同的角色获取不同的用户信息表
            LoginUser loginUser=new LoginUser();
            loginUser.setUserId(userId);
            try {
                if("agent".equals(roleId)){  //管理员（服务商）
                    if (!"agent".equals(ifbing.getUserType())){
                        return new ResultBean("2","您不是管理员,正在为您跳转业务员页面");
                    }
                    Map<String, Object> userInfo = merchantStatusService.getMerchantInfoByCustId(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                    return new ResultBean("1",loginUser);
                }
                if ("salesman".equals(roleId)) {  //业务员
                    if (!"salesman".equals(ifbing.getUserType())){
                        return new ResultBean("2","您不是业务员,正在为您跳转管理员页面");
                    }
                    TdSalesmanInfo userInfo = salesmanManagerService.getTdSalesmanInfoById(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                    return new ResultBean("1",loginUser);
                }
            } catch (Exception e) {
                System.out.println("登陆异常!");
            }
        }
        return new ResultBean("0","登陆失败");
    }
}
