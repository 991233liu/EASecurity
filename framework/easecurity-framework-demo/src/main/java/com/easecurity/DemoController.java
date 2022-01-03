package com.easecurity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.basis.UserDo;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    SomeDao service;

    @RequestMapping("/queryData")
    public List<Object> queryData() {
	return service.queryForList(null);
    }

//    @RequestMapping("/queryData2")
//    public List<MenuDo> queryData2() {
//	return menuService.loadAll();
//    }

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public void queryData3(HttpServletRequest request) {
	UserDo userDo = (UserDo) request.getSession().getAttribute("userdo");
	System.out.println("-----## 当前登录人为：" + userDo.user.account);
    }

}
