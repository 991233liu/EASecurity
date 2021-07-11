/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.r;

import java.io.Serializable;

/**
 * r_role_user表：人员小角色关系。
 *
 */
public class RoleUser implements Serializable {

    private static final long serialVersionUID = -4826810551780175344L;

    public Integer id;
    /**
     * 用户ID
     */
    public String usreid;
    /**
     * 小角色ID
     */
    public String roleid;
    /**
     * 登录账号
     */
    public String user;
    /**
     * 小角色编码
     */
    public String roleCode;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getUsreid() {
	return usreid;
    }

    public void setUsreid(String usreid) {
	this.usreid = usreid;
    }

    public String getRoleid() {
	return roleid;
    }

    public void setRoleid(String roleid) {
	this.roleid = roleid;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getRoleCode() {
	return roleCode;
    }

    public void setRoleCode(String roleCode) {
	this.roleCode = roleCode;
    }

}
