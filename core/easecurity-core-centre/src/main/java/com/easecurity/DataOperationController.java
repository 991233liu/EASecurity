package com.easecurity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.MenuService;
import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.basis.MenuDo;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.b.UserEnum;

@RestController
@RequestMapping("/demo")
public class DataOperationController {

    @Autowired
    SomeDao service;

    @Autowired
    MenuService menuService;
    @Autowired
    LoginService loginService;

    @RequestMapping("/queryData")
    public List<Object> queryData() {
	return service.queryForList(null);
    }

    @RequestMapping("/queryData2")
    public List<MenuDo> queryData2() {
	return menuService.loadAll();
    }

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public void queryData3(HttpServletRequest request) {
//	loginService.login("liulufeng", "1");
	UserDo userDo = loginService.login("liulufeng", "1");
	request.getSession(true).setAttribute("userdo", userDo);
	System.out.println("----## userDo.getAllIdentities()=" + userDo.allIdentities());
	System.out.println("----## userDo.user.pStatus=" + userDo.user.pStatus);
	System.out.println("----## userDo.user.pStatus=" + (userDo.user.pStatus==UserEnum.PStatus.启用));
    }

//    @PostMapping("/api/addData")
//    public String addData() {
//        try {
//            service.addData();
//            return "add data success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "add data fail";
//        }
//    }
}
