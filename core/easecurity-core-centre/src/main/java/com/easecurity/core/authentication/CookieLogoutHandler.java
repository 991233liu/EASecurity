/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * 基于cookie的登录，统一注销
 *
 */
@Service
public class CookieLogoutHandler implements LogoutHandler {
    private static final Logger log = LoggerFactory.getLogger(CookieLogoutHandler.class);

    @Autowired
    LogoutService logoutService;

    @Value("${easecurity.client.logout.url:}")
    private List<String> urls;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	try {
	    if (urls != null) {
		// 起个线程异步注销其它应用。不管其它应用是否注销成功，主中心必须注销成功
		new Thread("logout") {
		    public void run() {
			urls.forEach((it) -> {
			    try {
				logoutService.logoutByCookie(it, request.getCookies());
			    } catch (Exception e) {
				log.error("注销远端系统登录时，出现异常，url:" + it, e);
			    }
			});
		    }
		}.start();
	    }
	} catch (Exception e) {
	    log.error("注销远端系统登录时，出现异常", e);
	}
    }

}
