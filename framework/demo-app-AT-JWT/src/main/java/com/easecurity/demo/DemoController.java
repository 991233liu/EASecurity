package com.easecurity.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.authentication.LoginService;

@RestController
@RequestMapping("/demo")
@EaSecured(org = "{id:['3','4']}")
public class DemoController {

    @Autowired
    private LoginService loginService;

    /**
     * 三个条件（IP、org和roleGroup）为“or”的关系
     */
    @RequestMapping("/queryData1")
    @EaSecured(IP = { "128.0.0.1" }, org = "{id:['1','4']}", role = "{'code':'root#user'}")
    public UserDetails queryData1(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    /**
     * 三个条件（条件1、条件2）为“and”的关系，条件2内部两个为“or”关系
     */
    @RequestMapping("/queryData2")
    @EaSecured(IP = { "128.0.0.1", "128.0.0.3" })
    @EaSecured(org = "{id:['1','4']}", roleGroup = "{'code':'user'}")
    public UserDetails queryData2(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    /**
     * 使用类上的控制
     */
    @RequestMapping("/queryData3")
    public UserDetails queryData3(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

    /**
     * 匿名访问
     */
    @RequestMapping("/queryData4")
    @EaSecuredAnonymous
    public UserDetails queryData4(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }
    
    /**
     * 其它条件
     */
    @RequestMapping("/queryData5")
    @EaSecured(user = "{account:'TestUser123'}", posts = "{name:'部门经理'}")
    public UserDetails queryData5(HttpServletRequest request) {
	UserDetails userDetails = loginService.getLocalUserDetails(request.getSession());
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }
}
