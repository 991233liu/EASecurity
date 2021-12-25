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
    public String userId;
    /**
     * 小角色ID
     */
    public String roleId;
    /**
     * 登录账号
     */
    public String account;
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

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleid) {
	this.roleId = roleid;
    }

    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }

    public String getRoleCode() {
	return roleCode;
    }

    public void setRoleCode(String roleCode) {
	this.roleCode = roleCode;
    }

}
