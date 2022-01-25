/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.List;

import com.easecurity.core.basis.re.Menu;

/**
 * 前端展示用的domain：菜单
 *
 */
public class MenuVo extends Menu {

    private static final long serialVersionUID = 4077406328620001722L;

    /**
     * 是否有访问权限
     */
    public boolean hasPermission = true;
    /**
     * 是否有子菜单
     */
    public boolean hasChild = false;
    /**
     * 子菜单ID
     */
    public List<String> childMenuIds;
    /**
     * 子菜单
     */
    public List<MenuVo> childMenu;

    public MenuVo() {
    }

    public MenuVo(MenuDo menuDo) {
	this.id = menuDo.menu.id;
	this.name = menuDo.menu.name;
	this.code = menuDo.menu.code;
	this.level = menuDo.menu.level;
	this.sortNumber = menuDo.menu.sortNumber;
	this.openUrl = menuDo.menu.openUrl;
	this.icon = menuDo.menu.icon;
	this.parentId = menuDo.menu.parentId;
	this.fullName = menuDo.menu.fullName;
	this.status = menuDo.menu.status;
	this.displayStatus = menuDo.menu.displayStatus;
	this.disablePrompt = menuDo.menu.disablePrompt;
	this.noPermissionsPrompt = menuDo.menu.noPermissionsPrompt;
	this.accessType = menuDo.menu.accessType;
	if (menuDo.childMenuIds != null && !menuDo.childMenuIds.isEmpty()) {
//	    this.childMenuIds = menuDo.childMenuIds;
//	    this.childMenuIds = new ArrayList<>();
//	    this.childMenu = new ArrayList<>();
	    this.hasChild = true;
	}
    }

    public boolean isHasPermission() {
	return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
	this.hasPermission = hasPermission;
    }

    public boolean isHasChild() {
	return hasChild;
    }

    public void setHasChild(boolean hasChild) {
	this.hasChild = hasChild;
    }

    public List<String> getChildMenuIds() {
	return childMenuIds;
    }

    public void setChildMenuIds(List<String> childMenuIds) {
	this.childMenuIds = childMenuIds;
    }

    public List<MenuVo> getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(List<MenuVo> childMenu) {
        this.childMenu = childMenu;
    }

}
