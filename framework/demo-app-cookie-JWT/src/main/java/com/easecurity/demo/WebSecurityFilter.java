/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.JWT;
import com.easecurity.framework.authentication.AbsWebSecurityFilter;

/**
 * 认证Filter
 *
 */
@Component
@Order(1)
public class WebSecurityFilter extends AbsWebSecurityFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public JWT getCurrentUserJWTFromLocalStore(ServletRequest request) {
	// 不启用session，使用UUID作为主键存入Redis，UUID值放到cookie中
	// TODO 其它处理方式，请参考SaveUserJWT2LocalStore方法对应处理
	HttpServletRequest req = (HttpServletRequest) request;
	if(req.getCookies()!=null) {
	    for (Cookie cookie : req.getCookies()) {
		if ("SESSION_JWT".equals(cookie.getName())) {
		    return (JWT) redisTemplate.opsForValue().get("JWT:" + cookie.getValue());
		}
	    }
	}
	return null;
    }

    @Override
    public void SaveUserJWT2LocalStore(ServletRequest request, ServletResponse response, JWT jwt) {
	// TODO 启用session的，可以存入session，或者以sessionid作为主键存入Redis
	// TODO 没有启用session的，可以将JWT的hash值作为主键存入Redis中，将hash值放到cookie中返回前台
	// TODO 其它自己定义的处理方式

	// 不启用session，使用UUID作为主键存入Redis，UUID值放到cookie中返回前台
	Cookie cookie = new Cookie("SESSION_JWT", UUID.randomUUID().toString());
	cookie.setHttpOnly(true);
	cookie.setPath("/");
	HttpServletResponse rep = (HttpServletResponse)response;
	rep.addCookie(cookie);
	redisTemplate.opsForValue().set("JWT:" + cookie.getValue(), jwt, 300, TimeUnit.SECONDS);
    }

}
