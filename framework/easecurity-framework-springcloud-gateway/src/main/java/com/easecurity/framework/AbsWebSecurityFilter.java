/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.framework.authentication.LoginService;

import reactor.core.publisher.Mono;

/**
 * 认证Filter
 *
 */
public abstract class AbsWebSecurityFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AbsWebSecurityFilter.class);

    @Autowired
    EaSecurityConfiguration eaSecurityConfiguration;
    @Autowired
    LoginService loginService;

    RSAPublicKey rsaPublicKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
	log.debug("---------# AuthFilter.doFilter in");
	ServerHttpRequest request = exchange.getRequest();
	ServerHttpResponse response = exchange.getResponse();

	String uri = request.getURI().toString();
	Mono<Void> mono = Mono.empty();
	if (uri.endsWith("logout")) { // logout不拦截
	    mono = Mono.empty();
	} else {
	    JWT jwt = getCurrentUserJWTFromLocalStore(request);
	    if (jwt == null || !jwt.verify()) {
		// 未登录时或者JWT过期时，从远端认证中心拉取最新状态
		jwt = getCurrentUserJWTFromSecurityCentre(request);
		// 存入本地缓存
		if (jwt != null) {
		    SaveUserJWT2LocalStore(request, response, jwt);
		}
	    }
	    if (jwt == null || !jwt.verify()) {
		// 远端认证中心没有返回有效的身份时的处理
		mono = noLogin(request, response, jwt);
	    } else { // 已登录用户正常响应
		mono = addJWT2ServiceRequest(jwt.parsedStr, jwt, exchange, chain);
	    }
	}

	System.out.println("Inside ABCFilter: " + uri);
	log.debug("---------# AuthFilter.doFilter out");
	return mono;
    }

    /**
     * 从应用缓存中获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    public abstract JWT getCurrentUserJWTFromLocalStore(ServerHttpRequest request);

    /**
     * 将当前登录用户的JWT存入应用缓存
     * 
     * @param request
     * @param jwt
     */
    public abstract void SaveUserJWT2LocalStore(ServerHttpRequest request, ServerHttpResponse response, JWT jwt);

    /**
     * 将当前登录用户的JWT信息传递给后续的ServiceRequest
     * 
     * @param jwtStr   JWT密文
     * @param jwt      JWT明文
     * @param exchange
     * @param chain
     */
    public abstract Mono<Void> addJWT2ServiceRequest(String jwtStr, JWT jwt, ServerWebExchange exchange, GatewayFilterChain chain);

    public EaSecurityConfiguration getConfig() {
	return eaSecurityConfiguration;
    }

    /**
     * 从远端认证中心获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    public JWT getCurrentUserJWTFromSecurityCentre(ServerHttpRequest request) {
	try {
	    if (rsaPublicKey == null)
		rsaPublicKey = getRSAPublicKey();
	    long s = System.currentTimeMillis();
	    List<Cookie> cookies = new ArrayList<Cookie>();
	    MultiValueMap<String, HttpCookie> mcookies = request.getCookies();
	    if (mcookies != null && !mcookies.isEmpty()) {
		for (String k : mcookies.keySet()) {
		    cookies.add(new Cookie(k, mcookies.getFirst(k).getValue()));
		}
	    }
	    JWT jwt = loginService.getCurrentUserJWT(cookies.toArray(new Cookie[cookies.size()]), rsaPublicKey);
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
     * 未登录时的处理。（远端认证中心没有返回有效的身份时的处理）
     * 
     * @param request
     * @param response
     * @param chain
     * @param jwt      远端认证中心返回的JWT
     */
    public Mono<Void> noLogin(ServerHttpRequest request, ServerHttpResponse response, JWT jwt) {
	try {
	    if (eaSecurityConfiguration.noLoginUrl != null && !"".equals(eaSecurityConfiguration.noLoginUrl)) {
		response.setStatusCode(HttpStatus.SEE_OTHER);
		response.getHeaders().set("Location", eaSecurityConfiguration.noLoginUrl);
		return response.setComplete();
	    } else if (eaSecurityConfiguration.noLoginMessage != null && !"".equals(eaSecurityConfiguration.noLoginMessage)) {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		DataBuffer data = response.bufferFactory().wrap(eaSecurityConfiguration.noLoginMessage.getBytes());
		return response.writeWith(Mono.just(data));
	    } else {
		String uri = request.getURI().getPath();
		response.setStatusCode(HttpStatus.SEE_OTHER);
		response.getHeaders().set("Location", "/SecurityCentre/auth/login?srchref=" + URLEncoder.encode(uri, "GBK"));
		return response.setComplete();
	    }
	} catch (Exception e) {
	    log.error("在处理noLogin请求时，异常", e);
	}
	return Mono.empty();
    }

    private RSAPublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	File file = org.springframework.util.ResourceUtils.getFile(eaSecurityConfiguration.jwt.getPublicKey());
	if (file.isFile()) {
	    return loginService.getRSAPublicKey(file);
	} else {
	    return loginService.getRSAPublicKey(eaSecurityConfiguration.jwt.getPublicKey());
	}
    }
}
