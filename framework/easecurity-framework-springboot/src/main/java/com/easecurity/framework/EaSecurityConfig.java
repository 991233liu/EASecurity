/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.framework.access.UriService;

/**
 * 访问控制配置
 *
 */
@Configuration
public class EaSecurityConfig {
//    private static final Logger log = LoggerFactory.getLogger(AccessConfig.class);

    @Value("${easecurity.server.url:}")
    private String easCentreUrl;

    @Value("${easecurity.server.threadSleepTime:}")
    private String threadSleepTime;

    @Value("${easecurity.server.connectTimeout:}")
    private String connectTimeout;

    @Bean
    public EaSecurityServiceFactory eaSecurityServiceFactory() {
	EaSecurityConfiguration eaSecurityConfiguration = new EaSecurityConfiguration(easCentreUrl);
	if (!threadSleepTime.isEmpty())
	    eaSecurityConfiguration.setThreadSleepTime(Long.parseLong(threadSleepTime));
	if (!connectTimeout.isEmpty())
	    eaSecurityConfiguration.setConnectTimeout(Integer.parseInt(connectTimeout));
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
}
