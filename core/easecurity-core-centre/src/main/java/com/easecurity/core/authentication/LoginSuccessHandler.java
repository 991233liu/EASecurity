/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 登录成功后消息和跳转处理类。处理逻辑：
 * <p>
 * <ul>
 * <li>如果请求中带有“srchref”参数，则直接跳转此参数表示的链接；</li>
 * <li>如果请求中没有“srchref”参数，但是带有“ajaxsubmit”参数，则
 * <ul>
 * <li>值为“token”返回token，</li>
 * <li>值为……；</li>
 * </ul>
 * </li>
 * <li>如果什么参数也没有，则按照{@link org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler}处理</li>
 * </ul>
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
	String srchref = request.getParameter("srchref");
	String ajaxsubmit = request.getParameter("ajaxsubmit");
	if (srchref != null && !"".equals(srchref)) { // 跳转到目标想要的地址
	    clearAuthenticationAttributes(request);
	    srchref = response.encodeRedirectURL(srchref);
	    response.sendRedirect(srchref);
	} else if (ajaxsubmit != null && "token".equals(ajaxsubmit)) { // Ajax请求下，目标想要返回token
//	    // TODO if中增加Ajax报文头判断
//	    request.getHeader("Content-Type");
//	    request.getHeader("Accept");
	    // TODO 返回token
	} else {
	    super.onAuthenticationSuccess(request, response, authentication);
	}
    }
}
