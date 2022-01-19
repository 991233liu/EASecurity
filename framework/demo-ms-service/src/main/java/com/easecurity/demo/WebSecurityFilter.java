/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.JWT;
import com.easecurity.framework.AbsMSWebSecurityFilter;

/**
 * 认证Filter
 *
 */
@Component
@Order(1)
public class WebSecurityFilter extends AbsMSWebSecurityFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从HttpServletRequest中取出JWT密文，然后解码。
     * 考虑到每次解码都很浪费时间，可以将密文hash值作为主键存入redis缓存（缓存有效期要尽量小）。
     * hash算法尽量选择hash256以上的
     */
    @Override
    public JWT getCurrentUserJWTFromLocalStore(ServletRequest request) {
	// 不启用session，使用UUID作为主键存入Redis，UUID值放到cookie中
	// TODO 其它处理方式，请参考SaveUserJWT2LocalStore方法对应处理
//	HttpServletRequest req = (HttpServletRequest) request;
//	if(req.getCookies()!=null) {
//	    for (Cookie cookie : req.getCookies()) {
//		if ("SESSION_JWT".equals(cookie.getName())) {
//		    return (JWT) redisTemplate.opsForValue().get("JWT:" + cookie.getValue());
//		}
//	    }
//	}
	return null;
    }
}
