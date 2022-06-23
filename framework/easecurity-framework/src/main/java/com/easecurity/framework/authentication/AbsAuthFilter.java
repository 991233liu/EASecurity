/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

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
 * <li>已登录，放行；</li>
 * <li>未登录，可匿名访问，则放行；</li>
 * <li>未登录，登录用户可访问，则先从SecurityCentre获取登录信息，获取成功放行，失败跳转到登录也。</li>
 * </ul>
 *
 */
public abstract class AbsAuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AbsAuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	log.debug("---------# AuthFilter.init");
	Filter.super.init(filterConfig);
	List<Method> allMethod = loadAllUriWithAnnotation();
	if (allMethod != null) {
	    for (Method method : allMethod) {
		try {
		    saveUri(method);
		} catch (Exception e) {
		    log.error("保存EaSecured注解管理的URI到安全认证中心时异常：" + method.getDeclaringClass().getName() + method.getName() + method.toString(), e);
		}
	    }
	}
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	log.debug("---------# AuthFilter.doFilter in");

	HttpServletRequest req = (HttpServletRequest) request;
	String uri = req.getRequestURI();
	if (log.isDebugEnabled())
	    System.out.println("Inside ABCFilter: " + ((HttpServletRequest) request).getRequestURI());
	
	// 获取登录信息
	JWT jwt = getCurrentUserJWTFromLocalStore(request);
	if (!uri.endsWith("logout") && (jwt == null || !jwt.verify())) {
	    // 未登录时或者JWT过期时，从远端认证中心拉取最新状态
	    jwt = getCurrentUserJWTFromSecurityCentre(request);
	    // 存入本地缓存
	    if (jwt != null) {
		jwt.removeParsedStr();
		SaveUserJWT2LocalStore(request, response, jwt);
	    }
	}

	if (canAnonymousAccess(uri, request)) { // 可匿名访问的，直接放行
	    if (jwt != null && jwt.verify())
		LoginService.userDetails.set(jwt.userDetails);
	    try {
		chain.doFilter(request, response);
	    } finally {
		LoginService.userDetails.remove();
	    }
	} else { // 其它情况需要判断登录状态
	    if (jwt == null || !jwt.verify()) {
		// 远端认证中心没有返回有效的身份时的处理
		noLogin(request, response, chain, jwt);
	    } else { // 已登录用户正常响应
		LoginService.userDetails.set(jwt.userDetails);
		try {
		    chain.doFilter(request, response);
		} finally {
		    LoginService.userDetails.remove();
		}
	    }
	}
	log.debug("---------# AuthFilter.doFilter out");
    }

    @Override
    public void destroy() {
	log.debug("---------# AuthFilter.destroy");
	Filter.super.destroy();
    }

    public abstract EaSecurityConfiguration getConfig();

    /**
     * 加载所有EaSecured注解管理的URI，进行进行初始化
     * 
     * @return
     */
    public abstract List<Method> loadAllUriWithAnnotation();

    /**
     * 保存EaSecured注解管理的URI到安全认证中心
     */
    public abstract void saveUri(Method method);

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
     * 是否可匿名访问
     * 
     * @param uri
     * @param request
     * @return
     */
    public abstract boolean canAnonymousAccess(String uri, ServletRequest request);

    /**
     * 未登录时的处理。（远端认证中心没有返回有效的身份时的处理）
     * 
     * @param request
     * @param response
     * @param chain
     */
    public abstract void noLogin(ServletRequest request, ServletResponse response, FilterChain chain, JWT jwt);
}
