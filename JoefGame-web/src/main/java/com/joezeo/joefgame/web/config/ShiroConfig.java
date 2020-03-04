package com.joezeo.joefgame.web.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joezeo.joefgame.potal.shiro.CustomFormAuthenticationFilter;
import com.joezeo.joefgame.potal.shiro.GithubShiroRealm;
import com.joezeo.joefgame.potal.shiro.UserShiroRealm;
import com.joezeo.joefgame.common.utils.PasswordHelper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class ShiroConfig {

    @Value("${shiro.remember.cipher.key}")
    private String cipherKey;


    @Autowired
    private PasswordHelper passwordHelper;

    /**
     * CustomFormAuthenticationFilter 取消SpringBoot的自动注册
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy(CustomFormAuthenticationFilter customFormAuthenticationFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(customFormAuthenticationFilter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    /**
     *  自定义filter
     *  这个fiter暂时没有效果，不起作用
     *  猜想是因为这个filter直接拦截请求，而我自己已经写了控制器控制/login请求
     */
    @Bean
    public CustomFormAuthenticationFilter customFormAuthenticationFilter(){
        CustomFormAuthenticationFilter customFormAuthenticationFilter = new CustomFormAuthenticationFilter();
        customFormAuthenticationFilter.setLoginUrl("/login");
        customFormAuthenticationFilter.setUsernameParam("email");
        customFormAuthenticationFilter.setPasswordParam("password");
        customFormAuthenticationFilter.setRememberMeParam("rememberMe");
        return customFormAuthenticationFilter;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 注册自定义CustomFormAuthenticationFilter
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", customFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/"); // 如果未经验证，返回根页面
        shiroFilterFactoryBean.setSuccessUrl("/");

        filterChainDefinitionMap.put("/*", "anon");
        filterChainDefinitionMap.put("/home", "user");
        filterChainDefinitionMap.put("/profile/*", "user");
        filterChainDefinitionMap.put("/publish", "user");
        filterChainDefinitionMap.put("/manager/*", "roles[admin]"); // 进入管理页面需要角色admin验证
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(passwordHelper.getAlgorithm()); // 散列算法
        hashedCredentialsMatcher.setHashIterations(passwordHelper.getIteration()); // 散列次数
        return hashedCredentialsMatcher;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(60 * 60 * 24 * 7); // cookie默认保存7天
        cookieRememberMeManager.setCookie(simpleCookie);
        // 由于每次关闭重启应用都会重新生成一个cipherKey，这会导致rememberMe功能失效，故设置一个固定值
        cookieRememberMeManager.setCipherKey(Base64.decode(cipherKey));
        return cookieRememberMeManager;
    }

    @Bean
    public UserShiroRealm userShiroRealm() {
        UserShiroRealm shiroRealm = new UserShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public GithubShiroRealm githubShiroRealm(){
        return new GithubShiroRealm();
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 设置多个realm
        List<Realm> list = new ArrayList<>();
        list.add(userShiroRealm());
        list.add(githubShiroRealm());
        securityManager.setRealms(list);
        // 多Realm认证策略，AtLeastOneSuccessFulAtrategy 至少一个成功的策略-默认使用
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

}