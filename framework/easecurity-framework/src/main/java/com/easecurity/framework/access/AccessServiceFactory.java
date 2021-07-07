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

    public static synchronized UriService getUriService(String easCentreUrl) {
	if (uriService == null)
	    uriService = new UriService(getAccessRegister(easCentreUrl));
	return uriService;
    }

    private static AccessRegister getAccessRegister(String easCentreUrl) {
	if (accessRegister == null) { // 初始化
	    accessRegister = AccessRegister.getInstance(easCentreUrl);
	}
	if (accessRegister == null)
	    throw new RuntimeException("---## 初始化AccessRegister时失败，easCentreUrl=" + easCentreUrl);
	return accessRegister;
    }
}
