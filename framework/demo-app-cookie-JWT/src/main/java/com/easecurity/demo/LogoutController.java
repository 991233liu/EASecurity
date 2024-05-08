package com.easecurity.demo;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class LogoutController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO 其它处理方式，请参考WebSecurityFilter.SaveUserJWT2LocalStore方法对应处理

        // 不启用session，将JWT.jti值作为主键存入Redis中，将JWT.jti值放到cookie中返回前台
        if (request.getCookies() != null) {
            for (Cookie cookie1 : request.getCookies()) {
                if ("SESSION_JWT".equals(cookie1.getName())) {
                    redisTemplate.delete("JWT:" + cookie1.getValue());
                }
            }
        }

        // 如果认证中心主动的撤销了某个jwt的授权，则会将jwt.jti发送过来，要求客户端主动清理本地缓存
        // jwt.jti信息会放到header中
        String jti = request.getHeader("jti");
        if (jti != null) {
            redisTemplate.delete("JWT:" + jti);
        }
    }
}
