package com.easecurity.demo;

import java.io.IOException;
import java.util.UUID;

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

	// 不启用session，使用UUID作为主键存入Redis，UUID值放到cookie中返回前台
	if(request.getCookies()!=null) {
	    for (Cookie cookie1 : request.getCookies()) {
		if ("SESSION_JWT".equals(cookie1.getName())) {
		    redisTemplate.delete("JWT:" + cookie1.getValue());
		}
	    }
	}
	Cookie cookie = new Cookie("SESSION_JWT", UUID.randomUUID().toString());
	cookie.setHttpOnly(true);
	cookie.setPath("/");
	HttpServletResponse rep = (HttpServletResponse) response;
	rep.addCookie(cookie);
    }
}