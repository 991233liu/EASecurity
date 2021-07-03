/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.List;

import com.easecurity.core.basis.au.MenuOrg;
import com.easecurity.core.basis.re.Menu;

/**
 * domain：菜单
 *
 */
public class MenuDo implements Serializable {

    private static final long serialVersionUID = -7280953169179654117L;

    /**
     * 菜单信息
     */
    public Menu menu;

    /**
     * 有菜单权限的组织
     */
    public List<MenuOrg> menuOrg;
    
    /**
     * 子菜单ID，按显示顺序排序。
     */
    public List<String> childMenuIds;

    private String _auOrgStr;

    /**
     * 从组织判断是否拥有此菜单的权限。
     * 
     * @return true 有权限；false 无权限。
     */
    // TODO 需要优化，临时凑合着写的
    public boolean havePermissionThroughOrg(String orgId) {
	if (menuOrg != null) {
	    if (_auOrgStr == null) {
		_auOrgStr = ",";
		for (MenuOrg mo : menuOrg) {
		    if ("0".equals(mo.status))
			_auOrgStr += mo.orgid + ",";
		}
	    }
	    return _auOrgStr.indexOf("," + orgId + ",") > -1;
	} else
	    return false;
    }
}
