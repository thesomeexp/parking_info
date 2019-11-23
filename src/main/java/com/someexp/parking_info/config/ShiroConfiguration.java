package com.someexp.parking_info.config;

import com.someexp.parking_info.filter.ShiroAuthFilter;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.realm.JPARealm;
import com.someexp.parking_info.util.MagicVariable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfiguration {
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 重写验证的方法, 主要是一些资源的验证
        filters.put("authc", new ShiroAuthFilter());
        /**
         * Shiro内置过滤器, 可以实现权限相关的拦截器
         * 常用的过滤器:
         * anon: 无需认证(登录)可以访问
         * authc: 必须认证才可以访问
         * user: 如果使用remeberMe的功能可以直接访问
         * perms: 该资源必须得到资源权限才可以访问
         * role: 该资源必须得到角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();

        // ===============前后分离, 只做api接口的认证, 而不做页面的认证===========
        // 放行的页面
//        filterMap.put("/home", "anon");
//        filterMap.put("/login", "anon");
//        filterMap.put("/register", "anon");
//        filterMap.put("/listNearbyInfos", "anon");
        // 管理员才能访问的东西, 拦截后自动跳转到未授权页面
//        filterMap.put("/admin/**", "perms[admin]");

        // 放行的静态资源, 首张图片, css, js文件等
//        filterMap.put("/static/**", "anon");
        // 验证登录才能访问的静态资源, 详情图片等
        filterMap.put("/data/**", "authc");
        // 验证登录才能访问的api接口
        filterMap.put("/fore**", "authc");
        // 管理员才能访问的api接口
        filterMap.put("/admin**", "perms[admin]");

        filterMap.put("/**", "anon");

        //前后端分离, 这个我就不写了
        // 修改调整的登录页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
        // 设置未授权提示页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        // 关联realm
        securityManager.setRealm(getJPARealm());
        return securityManager;
    }

    // 负责用户的认证和权限的处理
    @Bean
    public JPARealm getJPARealm(){
        JPARealm myShiroRealm = new JPARealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    // 对密码进行编码的，防止密码在数据库里明码保存
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(MagicVariable.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(MagicVariable.HASH_TIMES);
        return hashedCredentialsMatcher;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}