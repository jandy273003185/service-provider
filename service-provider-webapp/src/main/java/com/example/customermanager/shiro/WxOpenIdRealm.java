package com.example.customermanager.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class WxOpenIdRealm extends AuthorizingRealm {



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //TODO 用户权限列表,普通信息等...
        return null;
    }


    /**
     * 鉴权   openid 判断是否用户是否已经绑定微信
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        String openid = (String) token.getPrincipal();
        //sysUserDao.getbyWxaOpenId 根据openid查询是否有已绑定的userid,有就时已绑定
//        SysUserEntity exUser = sysUserDao.getbyWxaOpenId(openid);
//        if (exUser != null) {
//            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(openid, "ok", this.getClass().getSimpleName());
//            return authcInfo;
//        } else {
//            return null;
//        }
        return null;
    }

}
