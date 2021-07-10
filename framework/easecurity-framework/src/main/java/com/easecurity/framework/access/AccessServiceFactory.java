/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

/**
 * 访问控制层服务工厂，用于获取访问控制service实例。
 *
 */
public class AccessServiceFactory {

    private static AccessRegister accessRegister = null;

//    private static AccessRegister accessRegister = AccessRegister.getInstance("http://127.0.0.1/SecurityCentre");
    private static UriService uriService = null;

    private AccessServiceFactory() {
    }

    public static synchronized UriService getUriService(EaSecurityConfiguration eaSecurityConfiguration) {
	if (uriService == null)
	    uriService = new UriService(getAccessRegister(eaSecurityConfiguration));
	return uriService;
    }

    private static AccessRegister getAccessRegister(EaSecurityConfiguration eaSecurityConfiguration) {
	if (accessRegister == null) { // 初始化
	    accessRegister = AccessRegister.getInstance(eaSecurityConfiguration);
	}
	if (accessRegister == null)
	    throw new RuntimeException("---## 初始化AccessRegister时失败，easCentreUrl=" + eaSecurityConfiguration.getEasCentreUrl());
	return accessRegister;
    }
    
    public EaSecurityConfiguration getEaSecurityConfiguration() {
	return accessRegister.getEaSecurityConfiguration();
    }
}
