/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.easecurity.core.authentication.JWT;
import com.easecurity.framework.authentication.AbsGatwayWebSecurityFilter;

import reactor.core.publisher.Mono;

/**
 * 认证Filter
 *
 */
@Component
public class WebSecurityFilter extends AbsGatwayWebSecurityFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 从应用缓存中获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    @Override
    public JWT getCurrentUserJWTFromLocalStore(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        System.out.println("--------# cookies" + cookies);
        if (cookies != null && cookies.containsKey("SESSION_JWT")) {
            return (JWT) redisTemplate.opsForValue().get("JWT:" + cookies.getFirst("SESSION_JWT").getValue());
        }
        return null;
    }

    /**
     * 将当前登录用户的JWT存入应用缓存
     * 
     * @param request
     * @param jwt
     */
    @Override
    public void SaveUserJWT2LocalStore(ServerHttpRequest request, ServerHttpResponse response, JWT jwt) {
        ResponseCookieBuilder cookieBuilder = ResponseCookie.from("SESSION_JWT", jwt.jti);
        cookieBuilder.path("/");
        cookieBuilder.httpOnly(true);
        response.addCookie(cookieBuilder.build());
        redisTemplate.opsForValue().set("JWT:" + jwt.jti, jwt, 300, TimeUnit.SECONDS);
    }

    /**
     * 将当前登录用户的JWT信息传递给后续的ServiceRequest
     * 
     * @param jwtStr   JWT密文
     * @param jwt      JWT明文
     * @param exchange
     * @param chain
     */
    @Override
    public Mono<Void> addJWT2ServiceRequest(String jwtStr, JWT jwt, ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO 每种服务器所支持的header大小都不一样，需要根据自己的实际情况考虑如何将JWT密文传递给后续应用
        // JWT密文最小1K+
        ServerHttpRequest host = exchange.getRequest().mutate().header("authorization", "Bearer " + jwtStr).header("jwt.jti", jwt.jti).build();
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }
}
