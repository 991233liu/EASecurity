/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.easecurity.framework.access.UriService;
import com.easecurity.framework.authentication.LoginService;

/**
 * 访问控制配置
 *
 */
@Configuration
public class EaSecurityConfig implements EnvironmentAware{
//    private static final Logger log = LoggerFactory.getLogger(AccessConfig.class);
    
    private EaSecurityConfiguration eaSecurityConfiguration;

    @Bean
    public EaSecurityConfiguration eaSecurityConfiguration() {
	return eaSecurityConfiguration;
    }
    
    @Bean
    public EaSecurityServiceFactory eaSecurityServiceFactory() {
	return new EaSecurityServiceFactory(eaSecurityConfiguration);
    }
    
    @Bean
    public UriService uriAccessService(EaSecurityServiceFactory eaSecurityServiceFactory) {
	return eaSecurityServiceFactory.getUriService();
    }
    
    @Bean
    public LoginService loginService(EaSecurityServiceFactory eaSecurityServiceFactory) {
	return eaSecurityServiceFactory.getLoginService();
    }
    
    /**
     * 读取"easecurity"配置
     */
    @Override
    public void setEnvironment(Environment environment) {
	// 如果配置文件不包含相关配置，则不进行任何操作
	if (environment.containsProperty("easecurity")) {
	    return;
	}
	eaSecurityConfiguration = Binder.get(environment).bind("easecurity", Bindable.of(EaSecurityConfiguration.class)).get();
    }
}
