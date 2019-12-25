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
import com.sevenpay.agentmanager.utils.verfycode.VerifyInfoConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 登陆
     * @param userName 账号
     * @param password 密码
     * @param openId 微信用户唯一标识
     * @param roleCode 用户角色agent管理员（服务商）/salesman业务员
     * @return
     */
    @RequestMapping("/loginBinding")
    public ResultBean<?> loginBinding(String userName, String password, String openId, String roleCode){
        //不同的角色获取不同的用户信息表
        LoginUser loginUser=new LoginUser();
        //登陆校验
        if ("agent".equals(roleCode)) {//服务商绑定
            UserDTO userInfo = userManager.login(userName, password, roleCode);
            if(!userInfo.isSuccess()){
                return new ResultBean<String>("0","账号或密码错误");
            }
            //查询该账号是否绑定openId
            boolean isBinding = loginManagerService.LogincheckIsBinding(userName, roleCode);
            if (isBinding) {
                return new ResultBean("0","该账号已经被绑定，请用之前微信登陆，如有疑问，请联系客服！");
            }
            UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId
            if (ifbing != null) {
                if (ifbing.getIfUnbind().equals("0")){
                    ifbing.setUserId(userInfo.getCustId());
                    ifbing.setOpenId(openId);
                    ifbing.setLoginType("1");
                    ifbing.setUserType(roleCode);
                    ifbing.setIfUnbind("1");
                    loginManagerService.updateBindingInfo(ifbing);
                    loginUser.setUserInfo(userInfo.getCustId());
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userInfo.getCustId(),openId);
                    loginUser.setToken(token);
                    return new ResultBean<>("1",loginUser) ;//
                }
            }else {
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
        }
        if ("salesman".equals(roleCode)){
            TdSalesmanInfo tdSalesmanInfo1 = new TdSalesmanInfo();
            tdSalesmanInfo1.setUserPhone(userName);
            List<TdSalesmanInfo> tdSalesmanInfos = salesmanManagerService.listTdSalesmanInfos(tdSalesmanInfo1);
            if (tdSalesmanInfos.size()>0) {
                for (TdSalesmanInfo salesmanInfo : tdSalesmanInfos) {//假如多个服务商下都有该业务员
                    if ("1".equals(salesmanInfo.getStatus())) {//如果有启用的
                        TdSalesmanInfo userInfo = salesmanManagerService.checkSalesmanLogin(userName, password,salesmanInfo.getCustId());
                        if(StringUtils.isEmpty(userInfo)){
                            return new ResultBean<String>("0","账号或密码错误");
                        }
                        if ("1".equals(userInfo.getStatus())){
                            //查询该账号是否绑定openId
                            boolean isBinding = loginManagerService.LogincheckIsBinding(userName, roleCode);
                            if (isBinding) {
                                return new ResultBean("0","该账号已经被绑定，请用之前微信登陆，如有疑问，请联系客服！");
                            }
                            UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定过openId
                            if (ifbing != null) {
                                if (ifbing.getIfUnbind().equals("0")){
                                    ifbing.setIfUnbind("1");
                                    ifbing.setUserId(userInfo.getSalesmanId());
                                    ifbing.setCustId(userInfo.getCustId());
                                    ifbing.setOpenId(openId);
                                    ifbing.setLoginType("1");
                                    ifbing.setUserType(roleCode);
                                    loginManagerService.updateBindingInfo(ifbing);
                                    loginUser.setUserId(userInfo.getSalesmanId());
                                    //根据用户编号和密码加密生成token
                                    String token = JWTUtil.sign(userInfo.getSalesmanId(), openId);
                                    loginUser.setToken(token);
                                    loginUser.setPhoneCode(userInfo.getUserPhone());
                                    return new ResultBean<>("1",loginUser) ;
                                }
                            }else {
                                UserLoginRelate userLoginRelate = new UserLoginRelate();
                                userLoginRelate.setUserId(userInfo.getSalesmanId());
                                userLoginRelate.setCustId(userInfo.getCustId());
                                userLoginRelate.setOpenId(openId);
                                userLoginRelate.setLoginType("1");
                                userLoginRelate.setUserType(roleCode);
                                userLoginRelate.setIfUnbind("1");
                                loginManagerService.userBinding(userLoginRelate);//用户绑定openId
                                loginUser.setUserId(userInfo.getSalesmanId());
                                //根据用户编号和密码加密生成token
                                String token = JWTUtil.sign(userInfo.getSalesmanId(), openId);
                                loginUser.setToken(token);
                                loginUser.setPhoneCode(userInfo.getUserPhone());
                                return new ResultBean<>("1",loginUser) ;
                            }
                        }
                        return new ResultBean<>("0","账号异常,请询问管理员！");
                    }
                }
            }

        }
        return new ResultBean<String>("0","绑定失败,请重新登陆");
    }


    /**
     * 第一步登陆
     * @param openId
     * @return
     */
    @RequestMapping("/login")
    public ResultBean<?> login(String openId,String roleId){

        UserLoginRelate ifbing = loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId

        if(ifbing == null){
            return new ResultBean<String>("0",openId);  //返回页面去登陆 进行绑定
        }else if ("0".equals(ifbing.getIfUnbind())){
            return new ResultBean<String>("0",openId); //返回页面重新绑定
        }else {
            String userId = ifbing.getUserId();//获取角色
            //不同的角色获取不同的用户信息表
            LoginUser loginUser=new LoginUser();
            loginUser.setUserId(userId);
            try {
                if("agent".equals(roleId)){  //管理员（服务商）
                    if (!"agent".equals(ifbing.getUserType())){
                        return new ResultBean<String>("2","您不是管理员,正在为您跳转业务员页面");
                    }
                    Map<String, Object> userInfo = merchantStatusService.getMerchantInfoByCustId(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                    return new ResultBean<LoginUser>("1",loginUser);
                }
                if ("salesman".equals(roleId)) {  //业务员
                    if (!"salesman".equals(ifbing.getUserType())){
                        return new ResultBean<String>("2","您不是业务员,正在为您跳转管理员页面");
                    }
                    TdSalesmanInfo userInfo = salesmanManagerService.getTdSalesmanInfoById(userId);
                    loginUser.setUserInfo(userInfo);
                    //根据用户编号和密码加密生成token
                    String token = JWTUtil.sign(userId,openId);
                    loginUser.setToken(token);
                    return new ResultBean<LoginUser>("1",loginUser);
                }
            } catch (Exception e) {
                System.out.println("登陆异常!");
            }
        }
        return new ResultBean<String>("0","登陆失败");
    }

    /**
     * 短信登陆
     * @param mobile 管理员（服务商）手机号
     * @param roleCode 角色 agent
     * @param openId openId
     * @param request
     * @return
     */
    @RequestMapping("/smsSpLogin")
    public ResultBean smsSpLoginBanding(String mobile, String roleCode,String openId, HttpServletRequest request){
        //查询是否有该服务商的信息
        UserDTO userInfo = userManager.getUserByEmailOrMobile(mobile, roleCode);
        if (userInfo.getCustId() != null) {
            //获取存入redis中的手机登录验证码
            String code = (String) redisTemplate.opsForValue().get(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
            if(code == null){
                return new ResultBean("0","验证码失效,请重新发送！");
            }
            String verifyCode = request.getParameter("verifyCode");
            if (StringUtils.isEmpty(verifyCode)) {
                return new ResultBean("0","请输入验证码");
            }
            if (code.equals(verifyCode)){
                LoginUser loginUser=new LoginUser();
                //登陆校验
                if ("agent".equals(roleCode)) {//服务商绑定
                    //查询该账号是否绑定openId
                    boolean isBinding = loginManagerService.LogincheckIsBinding(mobile, roleCode);
                    if (isBinding) {
                        return new ResultBean("0", "该账号已经被绑定，请用之前微信登陆，如有疑问，请联系客服！");
                    }
                    UserLoginRelate ifbing = loginManagerService.selectUserOpenid(openId);//查询是否有绑定openId
                    if (ifbing != null) {
                        if (ifbing.getIfUnbind().equals("0")) {
                            ifbing.setUserId(userInfo.getCustId());
                            ifbing.setOpenId(openId);
                            ifbing.setLoginType("1");
                            ifbing.setUserType(roleCode);
                            ifbing.setIfUnbind("1");
                            loginManagerService.updateBindingInfo(ifbing);
                            loginUser.setUserInfo(userInfo.getCustId());
                            //根据用户编号和密码加密生成token
                            String token = JWTUtil.sign(userInfo.getCustId(), openId);
                            loginUser.setToken(token);
                            redisTemplate.delete(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
                            return new ResultBean<>("1", loginUser);//
                        }
                    }else {
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
                        redisTemplate.delete(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
                        return new ResultBean<>("1",loginUser) ;//
                    }
                }
            }
        }
        return new ResultBean("0","请检查管理员手机号是否输入正确！");
    }

    /**
     * 业务员短信登陆
     * @param mobile 业务员手机号
     * @param roleCode 角色 agent
     * @param openId openId
     * @param request
     * @return
     */
    @RequestMapping("/smsSmLogin")
    public ResultBean smsSmLoginBanding(String mobile, String roleCode,String openId, HttpServletRequest request){
        LoginUser loginUser=new LoginUser();
        //1、查询是否有该业务员手机号
        TdSalesmanInfo tdSalesmanInfo = new TdSalesmanInfo();
        tdSalesmanInfo.setUserPhone(mobile);
        List<TdSalesmanInfo> tdSalesmanInfos = salesmanManagerService.listTdSalesmanInfos(tdSalesmanInfo);
        for (TdSalesmanInfo salesmanInfo : tdSalesmanInfos) {
            //2、查询该业务员在哪个商户下是启用状态
            if ("1".equals(salesmanInfo.getStatus())) {
                String custId = salesmanInfo.getCustId();//业务员所属服务商
                //3、查询该业务员账号是否有绑定过，如果有则修改
                boolean isBinding = loginManagerService.LogincheckIsBinding(mobile, roleCode);
                if (isBinding) {
                    return new ResultBean("0","该账号已经被绑定，请用之前微信登陆，如有疑问，请联系客服！");
                }
                //4、判断该手机号验证码操作是否匹配
                String code = (String) redisTemplate.opsForValue().get(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
                if(code == null){
                    return new ResultBean("0","验证码失效,请重新发送！");
                }
                String verifyCode = request.getParameter("verifyCode");
                if (StringUtils.isEmpty(verifyCode)) {
                    return new ResultBean("0","请输入验证码");
                }
                UserLoginRelate ifbing= loginManagerService.selectUserOpenid(openId);//查询是否有绑定过openId
                if (code.equals(verifyCode)){
                    //5、修改绑定信息
                    if (ifbing != null) {
                        if (ifbing.getIfUnbind().equals("0")){
                            ifbing.setIfUnbind("1");
                            ifbing.setUserId(salesmanInfo.getSalesmanId());
                            ifbing.setCustId(salesmanInfo.getCustId());
                            ifbing.setOpenId(openId);
                            ifbing.setLoginType("1");
                            ifbing.setUserType(roleCode);
                            loginManagerService.updateBindingInfo(ifbing);
                            loginUser.setUserId(salesmanInfo.getSalesmanId());
                            //根据用户编号和密码加密生成token
                            String token = JWTUtil.sign(salesmanInfo.getSalesmanId(), openId);
                            loginUser.setToken(token);
                            loginUser.setPhoneCode(salesmanInfo.getUserPhone());
                            redisTemplate.delete(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
                            return new ResultBean<>("1",loginUser) ;
                        }
                    }else {
                        UserLoginRelate userLoginRelate = new UserLoginRelate();
                        userLoginRelate.setUserId(salesmanInfo.getSalesmanId());
                        userLoginRelate.setCustId(salesmanInfo.getCustId());
                        userLoginRelate.setOpenId(openId);
                        userLoginRelate.setLoginType("1");
                        userLoginRelate.setUserType(roleCode);
                        userLoginRelate.setIfUnbind("1");
                        loginManagerService.userBinding(userLoginRelate);//用户绑定openId
                        loginUser.setUserId(salesmanInfo.getSalesmanId());
                        //根据用户编号和密码加密生成token
                        String token = JWTUtil.sign(salesmanInfo.getSalesmanId(), openId);
                        loginUser.setToken(token);
                        loginUser.setPhoneCode(salesmanInfo.getUserPhone());
                        redisTemplate.delete(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile);
                        return new ResultBean<>("1",loginUser) ;
                    }
                }
            }
        }
        return new ResultBean("0","请检查业务员手机号是否输入正确！");
    }

    /**
     * 管理员（服务商）修改密码接口
     * @param mobile 管理员（服务商）手机账号
     * @param newPw 新密码
     * @param roleCode 管理员角色标识（agent）
     * @param request
     * @return
     */
    @RequestMapping("forgetPassword")
    public ResultBean forgetPassword(String mobile,String newPw, String roleCode, HttpServletRequest request){
        //查询是否有该服务商的信息
        UserDTO userInfo = userManager.getUserByEmailOrMobile(mobile, roleCode);

        if (userInfo.getCustId() != null) {
            //获取redis中相对应的验证码
            String code = (String) redisTemplate.opsForValue().get(VerifyInfoConstant.FORGETPASSWORD_VERIFY_CODE+mobile);
            if(code == null){
                return new ResultBean("0","验证码失效,请重新发送！");
            }
            String verifyCode = request.getParameter("verifyCode");
            if (StringUtils.isEmpty(verifyCode)) {
                return new ResultBean("0","请输入验证码");
            }
      if (code.equals(verifyCode)) {
          String pw = userManager.updateUserPasswordByMobile(mobile, newPw, roleCode);
          if (pw == null){
              return new ResultBean("0","修改密码失败");
          }else {
              //删除redis中对应的key
              redisTemplate.delete(VerifyInfoConstant.FORGETPASSWORD_VERIFY_CODE+mobile);
              return new ResultBean("1",pw);
          }
      }
        }
        return new ResultBean("0","请检查管理员手机号是否输入正确！");
    }

}
