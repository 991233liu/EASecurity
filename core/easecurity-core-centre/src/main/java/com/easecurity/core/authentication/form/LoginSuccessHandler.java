/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.Token;
import com.easecurity.core.utils.ServletUtils;

/**
 * 登录成功后消息和跳转处理类。处理逻辑：
 * <p>
 * <ul>
 * <li>如果请求中带有“redirect_url”参数，则直接跳转此参数表示的链接；</li>
 * <li>如果什么参数也没有，则按照{@link org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler}处理</li>
 * </ul>
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
	String redirect_url = request.getParameter("redirect_url");
	String loginType = request.getParameter("loginType");
	// TODO 做个开关，选择走哪种认证模式，session(cookie)/accessToken
	if (loginType != null && "accessToken".equals(loginType)) { // AccessToken模式
	    Token token = loginService.creatToken(ServletUtils.getCurrentUser());
	    String str = JSON.toJSONString(token);
	    if (redirect_url != null && !"".equals(redirect_url)) {
		str = URLEncoder.encode(str, "GBK");
		if (redirect_url.indexOf("?") < 0)
		    redirect_url += "?";
		if (redirect_url.indexOf("&") < 0)
		    redirect_url += "1=1";
		redirect_url += "&access_token=" + token.access_token + "&token=" + str;
		response.setHeader("authorization", "Bearer " + token.access_token);
	    } else { // 没有指定跳转连接时，直接返回JSON串
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(str);
		return;
	    }
	} else { // 默认走session(cookie)
	    // 生成用户信息并缓存
	    ServletUtils.getCurrentUser();
	}
	if (redirect_url != null && !"".equals(redirect_url)) { // 跳转到目标想要的地址
	    clearAuthenticationAttributes(request);
	    redirect_url = response.encodeRedirectURL(redirect_url);
	    response.sendRedirect(redirect_url);
	} else {
	    super.onAuthenticationSuccess(request, response, authentication);
	}
    }
}
