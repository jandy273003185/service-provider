package com.sevenpay.agentmanager.config;


import org.springframework.context.annotation.Configuration;


/**
 * Shiro 配置bean
 */
@Configuration
public class ShiroConfigBean {

//    @Value("${images.relativePaths}")
//    private String relativePaths;
//    @Value("${images.uri}")
//    private String uri;
//
//    @Bean("shirFilter")
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        //拦截器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put(relativePaths,"anon");
//        filterChainDefinitionMap.put("/login/**", "anon");
//        //前后端带login登录的或者其他登录的通通放行
//        filterChainDefinitionMap.put("/**/login/**", "anon");
//        //filterChainDefinitionMap.put("/**/salesman/**", "anon");
//        filterChainDefinitionMap.put("/**/loginBinding/**", "anon");
//        filterChainDefinitionMap.put("/**.js", "anon");
//        filterChainDefinitionMap.put("/druid/**", "anon");
//        filterChainDefinitionMap.put("/swagger**/**", "anon");
//        filterChainDefinitionMap.put("/**/swagger**/**", "anon");
//        filterChainDefinitionMap.put("/webjars/**", "anon");
//        filterChainDefinitionMap.put("/v2/**", "anon");
//        filterChainDefinitionMap.put("/wx/**", "anon");//放行静态资源
//        filterChainDefinitionMap.put("/MP_verify_LVtMdY24lPWQfWpf.txt", "anon");//放行静态资源
//        // 添加自己的过滤器并且取名为jwt
//        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
//        filterMap.put("jwt", new JWTFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);
//        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
//        filterChainDefinitionMap.put("/**", "jwt");
//        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//
//
//    /**
//     * 注入安全管理器
//     * @param myRealm
//     * @return
//     */
//    @Bean("securityManager")
//    public SecurityManager securityManager(MyShiroRealm myRealm) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myRealm);
//
//        /*
//         * 关闭shiro自带的session，详情见文档
//         */
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        securityManager.setSubjectDAO(subjectDAO);
//        return securityManager;
//
//    }

}
