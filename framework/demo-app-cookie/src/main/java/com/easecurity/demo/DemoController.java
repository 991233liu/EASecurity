package com.easecurity.demo;

//import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
//import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.UserDetails;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public UserDetails queryData3(HttpServletRequest request) {
	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }

}
