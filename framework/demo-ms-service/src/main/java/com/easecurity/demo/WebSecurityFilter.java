/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.framework.AbsMSWebSecurityFilter;
import com.easecurity.framework.authentication.LoginService;

/**
 * 认证Filter
 *
 */
@Component
@Order(1)
public class WebSecurityFilter extends AbsMSWebSecurityFilter {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityFilter.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    LoginService loginService;

    /**
     * 从HttpServletRequest中取出JWT密文，然后解码。
     * 考虑到每次解码都很浪费时间，可以将密文hash值作为主键存入redis缓存（缓存有效期要尽量小）。 hash算法尽量选择hash256以上的
     */
    @Override
    public JWT getCurrentUserJWTFromLocalStore(ServletRequest request) {
	// 不使用缓存，直接解密
	HttpServletRequest req = (HttpServletRequest) request;
	String jwtStr = req.getHeader("Authorization");
	if (jwtStr != null && jwtStr.indexOf("Bearer") > -1) {
	    jwtStr = jwtStr.substring(jwtStr.indexOf("Bearer") + 6);
	    try {
		return loginService.getUserJWT(jwtStr, getRSAPublicKey());
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException | JWTExpirationException | IOException e) {
		log.error("获取登录人授权信息时异常",e);
	    }
	}
	return null;
    }
}
