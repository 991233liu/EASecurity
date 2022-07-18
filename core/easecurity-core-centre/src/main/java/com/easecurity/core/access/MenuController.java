/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.authentication.UserDetails;
import com.easecurity.core.basis.MenuService;
import com.easecurity.core.basis.MenuVo;
import com.easecurity.core.utils.CacheUtil;
import com.easecurity.core.utils.ServletUtils;

/**
 * 菜单接口服务，对外提供服务。
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 当前登录人的菜单
     */
    @RequestMapping("/myMenu")
    @SuppressWarnings("unchecked")
    public List<MenuVo> myMenu(HttpServletRequest request) {
        menuService.loadAll();
        // 获取当前登录人
        UserDetails user = ServletUtils.getCurrentUser();
        if (user == null) {
            // TODO 未登录时？
            return new ArrayList<>();
        }
        String rootMenuCode = request.getParameter("rootMenuCode") == null ? "" : request.getParameter("rootMenuCode");
        List<MenuVo> myMenu = (List<MenuVo>) CacheUtil.getSessionCache("myMenu:" + rootMenuCode);
        if (myMenu == null) {
            myMenu = menuService.getMenuByUser(user, rootMenuCode);
            CacheUtil.setSessionCache("myMenu:" + rootMenuCode, myMenu);
        }
        // 获取有权限的菜单
        return myMenu;
    }
}
