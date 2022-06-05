/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.framework.EaSecurityConfiguration;

/**
 * 认证Filter
 *
 */
public abstract class AbsWebSecurityFilter extends AbsAuthFilter {
    private static final Logger log = LoggerFactory.getLogger(AbsWebSecurityFilter.class);

    @Autowired
    private EaSecurityConfiguration eaSecurityConfiguration;
    @Autowired
    private LoginService loginService;

    private RSAPublicKey rsaPublicKey;

    @Override
    public EaSecurityConfiguration getConfig() {
	return eaSecurityConfiguration;
    }

    /**
     * 从远端认证中心获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    @Override
    public JWT getCurrentUserJWTFromSecurityCentre(ServletRequest request) {
	try {
	    if (rsaPublicKey == null)
		rsaPublicKey = getRSAPublicKey();
	    long s = System.currentTimeMillis();
	    JWT jwt = loginService.getCurrentUserJWT((HttpServletRequest) request, rsaPublicKey);
	    if (log.isDebugEnabled())
		System.out.println("-----## 登录消耗时间为：" + (System.currentTimeMillis() - s));
	    if (log.isDebugEnabled() && jwt != null && jwt.userDetails != null)
		System.out.println("-----## 当前登录人为：" + jwt.userDetails.account);
	    return jwt;
	} catch (IOException e) {
	    log.error("在处理JWT请求时，IO异常", e);
	} catch (JWTExpirationException e) {
	    log.error("在处理JWT请求时，SecurityCentre返回的JWT无效", e);
	} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	    log.error("在处理JWT请求时，RAS证书加载异常", e);
	}
	return null;
    }

    /**
     * 是否可匿名访问
     * 
     * @param uri
     * @param request
     * @return
     */
    @Override
    public boolean canAnonymousAccess(String uri, ServletRequest request) {
	return uri.endsWith("logout");
    }

    /**
     * 未登录时的处理。（远端认证中心没有返回有效的身份时的处理）
     * 
     * @param request
     * @param response
     * @param chain
     * @param jwt      远端认证中心返回的JWT
     */
    @Override
    public void noLogin(ServletRequest request, ServletResponse response, FilterChain chain, JWT jwt) {
	HttpServletResponse resp = (HttpServletResponse) response;
	HttpServletRequest req = (HttpServletRequest) request;
	try {
	    if (eaSecurityConfiguration.noLoginUrl != null && !"".equals(eaSecurityConfiguration.noLoginUrl)) {
		String uri = req.getRequestURI();
		// 如果是SecurityCentre跳转过来的请求，就不要再跳转回去了，避免死循环。
		if (uri.indexOf("/SecurityCentre/") < 0)
		    resp.sendRedirect(eaSecurityConfiguration.noLoginUrl);
		else
		    resp.setStatus(403);
	    } else if (eaSecurityConfiguration.noLoginMessage != null && !"".equals(eaSecurityConfiguration.noLoginMessage)) {
		resp.getWriter().write(eaSecurityConfiguration.noLoginMessage);
		resp.setStatus(403);
	    } else {
		String uri = req.getRequestURI();
		// 如果是SecurityCentre跳转过来的请求，就不要再跳转回去了，避免死循环。
		if (uri.indexOf("/SecurityCentre/") < 0)
		    resp.sendRedirect("/SecurityCentre/auth/login?redirect_url=" + URLEncoder.encode(uri, "GBK"));
		else
		    resp.setStatus(403);
	    }
	} catch (Exception e) {
	    log.error("在处理noLogin请求时，异常", e);
	}
    }

    public RSAPublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	File file = org.springframework.util.ResourceUtils.getFile(eaSecurityConfiguration.jwt.getPublicKey());
	if (file.isFile()) {
	    return loginService.getRSAPublicKey(file);
	} else {
	    return loginService.getRSAPublicKey(eaSecurityConfiguration.jwt.getPublicKey());
	}

    }
}
