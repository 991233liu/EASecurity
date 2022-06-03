/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 登录失败后消息和跳转处理类。处理逻辑：
 * <p>
 * <ul>
 * <li>如果带有failurehref参数，直接跳转此参数表示的链接；</li>
 * <li>如果没有failurehref参数且为Ajax请求，则返回Ajax消息；</li>
 * <li>其它情况，则按照{@link org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler}处理</li>
 * </ul>
 *
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
	String failurehref = request.getParameter("failurehref");
	String contentType = request.getHeader("Content-Type");
	String accept = request.getHeader("Accept");
	String loginType = request.getParameter("loginType");
	// TODO 做个开关，选择走哪种认证模式，session(cookie)/accessToken
	if (loginType != null && "accessToken".equals(loginType)) {

	} else { // 默认走session(cookie)

	}
	if (failurehref != null && !"".equals(failurehref)) { // 跳转到目标想要的地址
	    failurehref = response.encodeRedirectURL(failurehref);
	    response.sendRedirect(failurehref);
	} else if ((accept != null && accept.toLowerCase().indexOf("application/json") > -1)
		|| (contentType != null && contentType.toLowerCase().indexOf("application/json") > -1)) { // Ajax请求，直接返回错误消息
	    response.setCharacterEncoding("utf-8");
	    response.setStatus(HttpStatus.UNAUTHORIZED.value());
	    response.getWriter().write("{\"status\":401,\"code\":401,\"message\":\"");
	    response.getWriter().write(exception.getMessage());
	    response.getWriter().write("\"}");
	} else if (exception instanceof BadGifCaptchaException) {
	    getRedirectStrategy().sendRedirect(request, response, "/auth/login?errorGifCaptcha");
	} else {
	    getRedirectStrategy().sendRedirect(request, response, "/auth/login?error");
	}
    }
}
