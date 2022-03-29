/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easecurity.core.authentication.JWT;
import com.easecurity.framework.EaSecurityConfiguration;

/**
 * 
 * 登录认证连接器。当有请求进入时，判断是否已登录：
 * <P>
 * <ul>
 * <li>已登录，则放行；</li>
 * <li>未登录，则先从SecurityCentre获取登录信息，获取成功放行，失败跳转到登录也。</li>
 * </ul>
 *
 */
public abstract class AbsAuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AbsAuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	log.debug("---------# AuthFilter.init");
	Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	log.debug("---------# AuthFilter.doFilter in");

	HttpServletRequest req = (HttpServletRequest) request;
	String uri = req.getRequestURI();
	if (uri.endsWith("logout")) { // logout不拦截
	    chain.doFilter(request, response);
	} else {
	    JWT jwt = getCurrentUserJWTFromLocalStore(request);
	    if (jwt == null || !jwt.verify()) {
		// 未登录时或者JWT过期时，从远端认证中心拉取最新状态
		jwt = getCurrentUserJWTFromSecurityCentre(request);
		// 存入本地缓存
		if (jwt != null) {
		    jwt.removeParsedStr();		    
		    SaveUserJWT2LocalStore(request, response, jwt);
		}
	    }
	    if (jwt == null || !jwt.verify()) {
		// 远端认证中心没有返回有效的身份时的处理
		noLogin(request, response, chain, jwt);
	    } else { // 已登录用户正常响应
		LoginService.userDetails.set(jwt.userDetails);
		chain.doFilter(request, response);
		// TODO bug，放到finally
		LoginService.userDetails.remove();
	    }
	}
	System.out.println("Inside ABCFilter: " + ((HttpServletRequest) request).getRequestURI());
	log.debug("---------# AuthFilter.doFilter out");
    }

    @Override
    public void destroy() {
	log.debug("---------# AuthFilter.destroy");
	Filter.super.destroy();
    }

    public abstract EaSecurityConfiguration getConfig();

    /**
     * 从应用缓存中获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    public abstract JWT getCurrentUserJWTFromLocalStore(ServletRequest request);

    /**
     * 将当前登录用户的JWT存入应用缓存
     * 
     * @param request
     * @param jwt
     */
    public abstract void SaveUserJWT2LocalStore(ServletRequest request, ServletResponse response, JWT jwt);

    /**
     * 从远端认证中心获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    public abstract JWT getCurrentUserJWTFromSecurityCentre(ServletRequest request);
    
    /**
     * 未登录时的处理。（远端认证中心没有返回有效的身份时的处理）
     * 
     * @param request
     * @param response
     * @param chain
     */
    public abstract void noLogin(ServletRequest request, ServletResponse response, FilterChain chain, JWT jwt);
}
