package com.sevenpay.agentmanager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.UserLoginRelate;
import com.qifenqian.app.bean.customer.TdSalesmanInfo;
import com.qifenqian.app.bean.dto.UserDTO;
import com.qifenqian.app.customer.MerchantStatusService;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.qifenqian.app.login.UserLoginManagerService;
import com.qifenqian.app.user.UserManager;
import com.sevenpay.agentmanager.pojo.LoginUser;
import com.sevenpay.agentmanager.pojo.ResponseDataUtil;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.JWTUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class LoginController {

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
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("/loginBinding")
    public ResultBean loginBinding(String userName, String password, String openId, String roleCode){

        UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId

        if(ifbing == null){ //openid 没有被绑定
            //登陆校验
            if ("agent".equals(roleCode)) {//服务商绑定
                UserDTO userInfo = userManager.login(userName, password, roleCode);
                if(userInfo == null){
                    return new ResultBean("账号或密码错误");
                }
                UserLoginRelate userLoginRelate = new UserLoginRelate();
                userLoginRelate.setUserId(userInfo.getCustId());
                userLoginRelate.setOpenId(openId);
                userLoginRelate.setLoginType("1");
                userLoginRelate.setUserType(roleCode);
                userLoginRelate.setIfUnbind("1");
                loginManagerService.userBinding(userLoginRelate);//用户绑定openId
                return new ResultBean("绑定成功",userInfo.getCustId()) ;
            }
            if ("salesman".equals(roleCode)){
                TdSalesmanInfo userInfo = salesmanManagerService.checkSalesmanLogin(userName, password);
                if(userInfo == null){
                    return new ResultBean("账号或密码错误");
                }
                UserLoginRelate userLoginRelate = new UserLoginRelate();
                userLoginRelate.setUserId(String.valueOf(userInfo.getId()));
                userLoginRelate.setOpenId(openId);
                userLoginRelate.setLoginType("1");
                userLoginRelate.setUserType(roleCode);
                userLoginRelate.setIfUnbind("1");
                loginManagerService.userBinding(userLoginRelate);//用户绑定openId
                return new ResultBean("绑定成功",userInfo.getId()) ;
            }
        }
        String userId = ifbing.getUserId();
        return new ResultBean("登陆成功",userId);
    }


    /**
     * 第一步登陆
     * @param openId
     * @return
     */
    @RequestMapping("/login")
    public Object login(String openId){
        UserLoginRelate ifbing = loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId
        if(ifbing == null){
            return ResponseDataUtil.failure("openId未绑定！，请绑定",openId);  //返回页面去登陆 进行绑定
        }else {

            String roleCode = ifbing.getUserType();//获取角色id
            String userId = ifbing.getUserId();//获取角色
            //不同的角色获取不同的用户信息表
            LoginUser loginUser=new LoginUser();
            try {
                if(roleCode.equals("agent")){  //管理员（服务商）
                    Map<String, Object> userInfo = merchantStatusService.getMerchantInfoByCustId(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                }
                if (roleCode.equals("salesman")) {  //业务员
                    TdSalesmanInfo userInfo = salesmanManagerService.getTdSalesmanInfoById(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                }
                return loginUser;
            } catch (Exception e) {
                System.out.println("该用户名或者密码错误,请检查后再登录!");
            }
        }
        return ResponseDataUtil.failure("登陆错误~请重试");
    }
}
