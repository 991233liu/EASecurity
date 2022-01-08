package com.easecurity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.authentication.CustomUserDetails;
import com.easecurity.core.basis.MenuDo;
import com.easecurity.core.basis.MenuService;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.b.UserEnum;
import com.easecurity.core.utils.ServletUtils;

@RestController
@RequestMapping("/demo")
public class DataOperationController {

    @Autowired
    SomeDao service;

    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;

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
	CustomUserDetails user = ServletUtils.getCurrentUser();
	// 获取身份
	UserDo userDo = userService.getUserDoById(user.id);
	request.getSession(true).setAttribute("userdo", userDo);
	System.out.println("----## userDo.getAllIdentities()=" + userDo.allIdentities());
	System.out.println("----## userDo.user.pStatus=" + userDo.user.pdStatus);
	System.out.println("----## userDo.user.pStatus=" + (userDo.user.pdStatus == UserEnum.PdStatus.ENABLED));
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
