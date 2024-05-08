/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.easecurity.core.authentication.JWT;

/**
 * 微服务框架ServiceServer认证处理Filter
 *
 */
public abstract class AbsMSWebSecurityFilter extends AbsWebSecurityFilter {

    /**
     * 将当前登录用户的JWT存入应用缓存
     */
    @Override
    public void SaveUserJWT2LocalStore(ServletRequest request, ServletResponse response, JWT jwt) {
    }

    /**
     * 从远端认证中心获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    @Override
    public JWT getCurrentUserJWTFromSecurityCentre(ServletRequest request) {
        // 微服务架构中，每次请求都带着JWT信息，没带的说明是未授权的请求
        // 不用再去认证中心认证了。网关已经认证过了。
        // 这个方法很重要，必须要存在，不然框架默认去认证中心认证。微服务下浪费性能
        return null;
    }
}
