/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.JWT;
import com.easecurity.framework.authentication.AbsWebSecurityFilter;
import com.easecurity.framework.authentication.LoginService;

/**
 * 认证Filter
 *
 */
@Component
@Order(1)
public class WebSecurityFilter extends AbsWebSecurityFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginService loginService;

    @Override
    public JWT getCurrentUserJWTFromLocalStore(ServletRequest request) {
	// 启用AccessToken的，可以将AccessToken作为主键存入Redis中
	String key = loginService.getAccessToken((HttpServletRequest) request);
	if (key != null)
	    return (JWT) redisTemplate.opsForValue().get("JWT:" + key);
	else
	    return null;
    }

    @Override
    public void SaveUserJWT2LocalStore(ServletRequest request, ServletResponse response, JWT jwt) {
	// TODO 启用AccessToken的，可以将AccessToken作为主键存入Redis中
	// TODO 其它自己定义的处理方式

	// 启用AccessToken的，可以将AccessToken作为主键存入Redis中
	String key = loginService.getAccessToken((HttpServletRequest) request);
	redisTemplate.opsForValue().set("JWT:" + key, jwt, 300, TimeUnit.SECONDS);
    }

}
