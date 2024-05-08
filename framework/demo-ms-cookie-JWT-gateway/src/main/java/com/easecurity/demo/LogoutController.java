package com.easecurity.demo;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies != null && cookies.containsKey("SESSION_JWT")) {
            redisTemplate.delete("JWT:" + cookies.getFirst("SESSION_JWT").getValue());
        }

        // 如果认证中心主动的撤销了某个jwt的授权，则会将jwt.jti发送过来，要求客户端主动清理本地缓存
        // jwt.jti信息会放到header中
        String jti = request.getHeaders().getFirst("jti");
        if (jti != null) {
            redisTemplate.delete("JWT:" + jti);
        }
    }
}
