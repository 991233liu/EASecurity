package com.easecurity.demo;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.framework.authentication.LoginService;

@RestController
@RequestMapping("/demo")
public class LogoutController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginService loginService;

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
	// TODO 其它处理方式，请参考WebSecurityFilter.SaveUserJWT2LocalStore方法对应处理

	// 启用AccessToken的，可以将AccessToken作为主键存入Redis中
	String key = loginService.getAccessToken((HttpServletRequest) request);
	System.out.println(key);
	redisTemplate.delete("JWT:" + key);
    }
}
