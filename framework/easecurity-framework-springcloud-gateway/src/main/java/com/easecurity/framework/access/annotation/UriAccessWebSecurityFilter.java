/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access.annotation;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.EaSecurityConfiguration;
import com.easecurity.framework.access.UriService;
import com.easecurity.framework.authentication.LoginService;

import reactor.core.publisher.Mono;

/**
 * 认证Filter
 *
 */
@Component
public class UriAccessWebSecurityFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(UriAccessWebSecurityFilter.class);

    @Resource
    UriService uriAccessService;
    @Resource
    LoginService loginService;
    @Resource
    EaSecurityConfiguration eaSecurityConfiguration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
	log.debug("---------# UriAccessWebSecurityFilter.filter in");
	ServerHttpRequest request = exchange.getRequest();
	ServerHttpResponse response = exchange.getResponse();
	Mono<Void> mono = Mono.empty();
	try {
	    String uri = request.getURI().getPath();
	    UserDetails user = loginService.getLocalUserDetails(null);
	    log.debug("UriAccessWebSecurityFilter, uri={} loginUser={}", uri, user);

	    if (uriAccessService.validation(uri, user)) { // 有执行权限
		if (eaSecurityConfiguration.isDevelopmentMode()) { // 开发模式
		    log.info("---## 恭喜你，权限校验通过。当前校验模式为{}", eaSecurityConfiguration.verification);
		}
		mono = chain.filter(exchange);
	    } else { // 无执行权限
		if (eaSecurityConfiguration.isDevelopmentMode()) { // 开发模式
		    log.info("---## 很遗憾，权限校验未通过。你收到了一次非法请求，被请求方法为{}，当前登录人为{}，当前校验模式为{}", uri, user, eaSecurityConfiguration.verification);
		}
		response.setStatusCode(HttpStatus.FORBIDDEN);
		DataBuffer data = response.bufferFactory().wrap(eaSecurityConfiguration.noPermissionMessage.getBytes());
		mono = response.writeWith(Mono.just(data));
	    }
	} catch (Exception e) {
	    log.error("UriAccessWebSecurityFilter 执行时出现异常：", e);
	} finally {
	    if (eaSecurityConfiguration.isDevelopmentMode()) // 开发模式
		mono = chain.filter(exchange);
	}
	
	log.debug("---------# UriAccessWebSecurityFilter.filter out");
	return mono;
    }

    @Override
    public int getOrder() {
	return 1;
    }

}
