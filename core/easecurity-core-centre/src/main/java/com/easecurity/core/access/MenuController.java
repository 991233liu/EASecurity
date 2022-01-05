/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.basis.MenuDo;
import com.easecurity.core.basis.MenuService;
import com.easecurity.core.basis.UserDo;

/**
 * 菜单接口服务，对外提供服务。
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @Autowired
    LoginService loginService;
    
    /**
     * 当前登录人的菜单
     */
    @RequestMapping("/myMenu")
    public List<MenuDo> myMenu(HttpServletRequest request) {
	menuService.loadAll();
	// TODO 获取当前登录人
	// TODO 获取身份
	UserDo userDo = loginService.login("liulufeng", "1");
	request.getSession(true).setAttribute("userdo", userDo);
	// TODO 获取有权限的菜单
	return menuService.getMenuByUser(userDo);
    }
}
