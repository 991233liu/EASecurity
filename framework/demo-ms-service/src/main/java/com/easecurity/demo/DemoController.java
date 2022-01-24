package com.easecurity.demo;

//import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
//import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.authentication.LoginService;

@RestController
@RequestMapping("/demo")
@EaSecured(org = "{id:['3','4']}")
public class DemoController {

    @Autowired
    LoginService loginService;

    /**
     * 两个条件（IP和org）为“or”的关系
     */
    @RequestMapping("/queryData1")
    @EaSecured(IP = { "128.0.0.1" }, org = "{id:['1','4']}")
    public UserDetails queryData1(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    /**
     * 两个条件（IP和org）为“and”的关系
     */
    @RequestMapping("/queryData2")
    @EaSecured(IP = { "128.0.0.3" })
    @EaSecured(org = "{id:['1','4']}")
    public UserDetails queryData2(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    @RequestMapping("/queryData3")
    public UserDetails queryData3(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    @RequestMapping("/queryData4")
    @EaSecured(org = "{id:['2','8'], code:['abumen','bbumen']}")
    public UserDetails queryData4(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }
}
