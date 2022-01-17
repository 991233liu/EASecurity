package com.easecurity.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.authentication.LoginService;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    LoginService loginService;

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public UserDetails queryData3(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }
}
