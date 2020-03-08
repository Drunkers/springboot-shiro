package com.example.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.sun.javafx.collections.MappingChange;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //ShirFilterBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的其余过滤器
        //anon:无需认证
        //authc:需要认证
        //perms:需要某些资源，授权

        //拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权，需要带有add，才能去add页面
        filterMap.put("/admin/add","perms[add]");
        filterMap.put("/admin/update","perms[update]");

        filterMap.put("/admin/*","authc");
        bean.setFilterChainDefinitionMap(filterMap);

        //设置登录验证
        bean.setLoginUrl("/toLogin");
        //设置未授权,如未授权则跳转该页面
        bean.setUnauthorizedUrl("/nopers");

        return bean;
    }
    //DefaultManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;

    }
    //realm对象（自定义）
    @Bean
    public UserRealm userRealm(){
        return  new UserRealm();
    }

    @Bean
    //整合shiroDialect 、用来整合shiro thymeleaf
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}
