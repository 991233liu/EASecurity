package com.easecurity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authorization.MenuService;
import com.easecurity.core.basis.MenuDo;

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
    public void queryData3() {
	loginService.login("liulufeng", "1");
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
