/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.LogoutService;
import com.easecurity.core.utils.CacheUtil;
import com.easecurity.core.utils.ServletUtils;

/**
 * 统一注销
 *
 */
@Component
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {
//    private static final Logger log = LoggerFactory.getLogger(CookieLogoutHandler.class);

    @Autowired
    private LogoutService logoutService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	String jti = (String) CacheUtil.getSessionCache("JWT.jti");
	String at = ServletUtils.getAccessToken();
	logoutService.asynLogout(request.getCookies(), at, jti);
	CacheUtil.delAccessTokenCache(at);
    }
}
