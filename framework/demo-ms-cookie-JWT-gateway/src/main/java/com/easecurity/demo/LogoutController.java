package com.easecurity.demo;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@RequestMapping("/demo")
public class LogoutController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/logout")
    public void logout(ServerWebExchange exchange) throws IOException {
	// 不启用session，使用UUID作为主键存入Redis，UUID值放到cookie中返回前台
	ServerHttpRequest request = exchange.getRequest();
	ServerHttpResponse response = exchange.getResponse();
	MultiValueMap<String, HttpCookie> cookies = request.getCookies();
	if (cookies != null && cookies.containsKey("SESSION_JWT")) {
	    redisTemplate.delete("JWT:" + cookies.getFirst("SESSION_JWT").getValue());
	}
	String id = UUID.randomUUID().toString();
	ResponseCookieBuilder cookieBuilder = ResponseCookie.from("SESSION_JWT", id);
	cookieBuilder.path("/");
	cookieBuilder.httpOnly(true);
	response.addCookie(cookieBuilder.build());
    }
}
