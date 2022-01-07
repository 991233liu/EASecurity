/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import com.easecurity.framework.EaSecurityConfiguration;

/**
 * 登录相关服务
 *
 */
public class LoginService {

//    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private EaSecurityConfiguration eaSecurityConfiguration;

    public LoginService(EaSecurityConfiguration eaSecurityConfiguration) {
	this.eaSecurityConfiguration = eaSecurityConfiguration;
    }

}
