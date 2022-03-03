/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.LogoutService;

/**
 * 基于cookie的登录，统一注销
 *
 */
@Component
public class CookieLogoutHandler implements LogoutHandler {
//    private static final Logger log = LoggerFactory.getLogger(CookieLogoutHandler.class);

    @Autowired
    private LogoutService logoutService;

    @Value("${easecurity.client.logout.url:}")
    private List<String> urls;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	logoutService.asynLogoutByCookie(urls, request.getCookies());
    }
}
