/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.framework.access.AccessRegister;
import com.easecurity.framework.access.UriService;

/**
 * 访问控制层服务工厂，用于获取访问控制service实例。
 *
 */
public class EaSecurityServiceFactory {

    private EaSecurityConfiguration eaSecurityConfiguration;
    private AccessRegister accessRegister = null;
//    private static AccessRegister accessRegister = AccessRegister.getInstance("http://127.0.0.1/SecurityCentre");
    private UriService uriService = null;
    private LoginService loginService = null;

    public EaSecurityServiceFactory(EaSecurityConfiguration eaSecurityConfiguration) {
	this.eaSecurityConfiguration = eaSecurityConfiguration;
    }

    public synchronized UriService getUriService() {
	if (uriService == null)
	    uriService = new UriService(getAccessRegister());
	return uriService;
    }

    public synchronized LoginService getLoginService() {
	if (loginService == null)
	    loginService = new LoginService(getEaSecurityConfiguration());
	return loginService;
    }

    private AccessRegister getAccessRegister() {
	if (accessRegister == null) { // 初始化
	    accessRegister = AccessRegister.getInstance(getEaSecurityConfiguration());
	}
	if (accessRegister == null)
	    throw new RuntimeException("---## 初始化AccessRegister时失败，easCentreUrl=" + eaSecurityConfiguration.getEasCentreUrl());
	return accessRegister;
    }

    public EaSecurityConfiguration getEaSecurityConfiguration() {
	if (eaSecurityConfiguration == null)
	    new NullPointerException("EaSecurityConfiguration is null");
	return eaSecurityConfiguration;
    }
}
