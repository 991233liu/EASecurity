/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 访问控制配置
 *
 */
@Configuration
public class AccessConfig {
//    private static final Logger log = LoggerFactory.getLogger(AccessConfig.class);

    @Value("${easecurity.server.url:}")
    private String easCentreUrl;

    @Value("${easecurity.server.threadSleepTime:}")
    private String threadSleepTime;

    @Value("${easecurity.server.connectTimeout:}")
    private String connectTimeout;

    @Bean
    public UriService uriAccessService() {
	EaSecurityConfiguration eaSecurityConfiguration = new EaSecurityConfiguration(easCentreUrl);
	if (!threadSleepTime.isEmpty())
	    eaSecurityConfiguration.setThreadSleepTime(Long.parseLong(threadSleepTime));
	if (!connectTimeout.isEmpty())
	    eaSecurityConfiguration.setConnectTimeout(Integer.parseInt(connectTimeout));
	UriService uriService = AccessServiceFactory.getUriService(eaSecurityConfiguration);
	return uriService;
    }
}
