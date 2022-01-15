package com.easecurity.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.UserDetails;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    public void login(HttpServletRequest request) throws IOException {
	UserDetails userDetails = loginService.getCurrentUser(request);
	if (userDetails == null) {
	    System.out.println("anonymousUser");
	} else {
	    request.getSession(true).setAttribute("userDetails", userDetails);
	    System.out.println("-----## 当前登录人为：" + userDetails.account);
	}
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session) throws IOException {
	session.invalidate();
    }

//    @RequestMapping("/queryData")
//    public List<Object> queryData() {
//	return service.queryForList(null);
//    }

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public void queryData3(HttpServletRequest request) {
	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
    }

}
