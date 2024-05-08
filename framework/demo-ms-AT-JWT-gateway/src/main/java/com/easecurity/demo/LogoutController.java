package com.easecurity.demo;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@RequestMapping("/demo")
public class LogoutController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    WebSecurityFilter webSecurityFilter;

    @RequestMapping("/logout")
    public void logout(ServerWebExchange exchange) throws IOException {
        // TODO 其它处理方式，请参考WebSecurityFilter.SaveUserJWT2LocalStore方法对应处理
        // 启用AccessToken的，可以将AccessToken作为主键存入Redis中
        ServerHttpRequest request = exchange.getRequest();
        String key = webSecurityFilter.getAccessToken(request);
        System.out.println(key);
        redisTemplate.delete("JWT:" + key);
    }

}
