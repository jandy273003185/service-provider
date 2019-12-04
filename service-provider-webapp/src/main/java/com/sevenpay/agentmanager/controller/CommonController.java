package com.sevenpay.agentmanager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qifenqian.app.bean.Bank;
import com.qifenqian.app.bean.TbBankProvincesInfoBean;
import com.qifenqian.app.bean.TbProvincesInfoBean;
import com.qifenqian.app.bean.customer.TdSalesmanInfo;
import com.qifenqian.app.bean.dto.MessageDTO;
import com.qifenqian.app.bean.dto.UserDTO;
import com.qifenqian.app.common.BankInfoService;
import com.qifenqian.app.common.MessageManager;
import com.qifenqian.app.customer.MerchantInfoService;
import com.qifenqian.app.customer.SalesmanManagerService;
import com.qifenqian.app.user.UserManager;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.GenSN;
import com.sevenpay.agentmanager.utils.RedisUtil;
import com.sevenpay.agentmanager.utils.verfycode.VerifyInfoConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Reference
    private BankInfoService bankInfoService;

    @Reference
    private MessageManager messageManager;

    @Reference
    private UserManager userManager;

    @Reference
    private SalesmanManagerService salesmanManagerService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 省市县三级下拉
     * @param tbProvincesInfoBean
     * @return
     */
    @RequestMapping("province")
    public ResultBean<?> provinceQuery(TbProvincesInfoBean tbProvincesInfoBean){
        List<TbProvincesInfoBean> provinces = merchantInfoService.getProvinces(tbProvincesInfoBean);
        return new ResultBean<List<TbProvincesInfoBean>>("1",provinces);
    }

    /**
     * 银行省市二级下拉
     * @param tbBankProvincesInfoBean
     * @return
     */
    @RequestMapping("bankProvince")
    public ResultBean<?> provinceQuery(TbBankProvincesInfoBean tbBankProvincesInfoBean){
        List<TbBankProvincesInfoBean> bankProvinces = merchantInfoService.getBankProvinces(tbBankProvincesInfoBean);
        return new ResultBean<List<TbBankProvincesInfoBean>>("1",bankProvinces);
    }

    /**
     * 查询总行（带模糊）
     * @param bank
     * @return
     */
    @RequestMapping("bankHeadOffice")
    public ResultBean bankHeadOffice(Bank bank){
        List<Bank> banks = bankInfoService.selectBanks(bank);
        if (banks.size() > 0) {
            return new ResultBean("1",banks);
        }
        return new ResultBean("0");
    }

    /**
     * 查询支行（带模糊）
     * @param bank
     * @return
     */
    @RequestMapping("bankBranch")
    public ResultBean bankBranch(Bank bank){
        List<Bank> banks = bankInfoService.selectBranchBanks(bank);
        if (banks.size() > 0) {
            return new ResultBean("1",banks);
        }
        Bank bankHeadOffice = bankInfoService.selectBankByBankCode(bank.getBankCode());
        bankHeadOffice.setBranchBankCode(bankHeadOffice.getBankCode());
        bankHeadOffice.setBranchBankName(bankHeadOffice.getBankName());
        banks.add(bankHeadOffice);
        return new ResultBean("1",banks);
    }

    /**
     * 发送短信验证码登陆接口
     * @param request
     * @param messageDTO
     * @return
     */
    @RequestMapping("verifyCode")
    public ResultBean verifyCode(HttpServletRequest request, MessageDTO messageDTO){
        String mobile = request.getParameter("mobile");//手机号
        String roleCode = request.getParameter("roleCode");//角色（agent/salesman）
        if ("agent".equals(roleCode)) {
            //查询是否有该手机账号
            UserDTO userInfo = userManager.getUserByEmailOrMobile(mobile, roleCode);

            if (userInfo.getCustId() != null) {
                //生成验证码
                String smsVerifyCode = GenSN.getRandomNum(6);
                redisTemplate.opsForValue().set(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile, smsVerifyCode,3*60, TimeUnit.SECONDS);
                messageDTO.setType("login");
                messageDTO.setCode(smsVerifyCode);
                MessageDTO m = messageManager.sendMobileCode(messageDTO);
                return new ResultBean("1","短信发送成功");
            }
        }else if ("salesman".equals(roleCode)){
            //登录查询该业务员手机号是否有启用（只能有一个，该操作在业务员管理做了限制）
            TdSalesmanInfo tdSalesmanInfo1 = new TdSalesmanInfo();
            tdSalesmanInfo1.setUserPhone(mobile);
            List<TdSalesmanInfo> tdSalesmanInfos = salesmanManagerService.listTdSalesmanInfos(tdSalesmanInfo1);
            if (tdSalesmanInfos.size()>0) {
                for (TdSalesmanInfo salesmanInfo : tdSalesmanInfos) {//假如多个服务商下都有该业务员
                    if ("1".equals(salesmanInfo.getStatus())) {
                        String smsVerifyCode = GenSN.getRandomNum(6);
                        redisTemplate.opsForValue().set(VerifyInfoConstant.LOGIN_VERIFY_CODE+mobile, smsVerifyCode,3*60, TimeUnit.SECONDS);
                        messageDTO.setType("login");
                        messageDTO.setCode(smsVerifyCode);
                        MessageDTO m = messageManager.sendMobileCode(messageDTO);
                        return new ResultBean("1","短信发送成功");
                    }
                }
                return new ResultBean("0","该业务员账号无效");
            }
        }

       return new ResultBean("0","请检查管理员手机号是否输入正确！");
    }

    /**
     * 管理员忘记密码接口
     * @param request
     * @param messageDTO
     * @return
     */
    @RequestMapping("modifyPassword")
    public ResultBean modifyPassword(HttpServletRequest request, MessageDTO messageDTO){
        String mobile = request.getParameter("mobile");//服务商（管理员）手机号
        String roleCode = request.getParameter("roleCode");//角色（agent）
        //查询是否有该手机账号
        UserDTO userInfo = userManager.getUserByEmailOrMobile(mobile, roleCode);

        if (userInfo.getCustId() != null) {
            //生成验证码
            String smsVerifyCode = GenSN.getRandomNum(6);
            RedisUtil redisUtil = new RedisUtil();
            //redis存储验证码
            redisTemplate.opsForValue().set(VerifyInfoConstant.FORGETPASSWORD_VERIFY_CODE+mobile, smsVerifyCode,3*60, TimeUnit.SECONDS);
            messageDTO.setType("modify");
            messageDTO.setCode(smsVerifyCode);
            MessageDTO m = messageManager.sendMobileCode(messageDTO);
            return new ResultBean("1","短信发送成功");
        }
        return new ResultBean("0","请检查管理员手机号是否输入正确！");
    }
}
