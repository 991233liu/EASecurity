/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.r;

import java.io.Serializable;

/**
 * r_role表：小角色信息。
 *
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -5985962922034933727L;

    /**
     * 小角色主键=组织ID+#+大角色ID
     */
    public String id;
    /**
     * 角色名称
     */
    public String name;
    /**
     * 小角色编码=组织编码+#+大角色编码
     */
    public String code;
    /**
     * 小角色全称=组织名称+角色名称
     */
    public String fullName;
    /**
     * 大角色ID
     */
    public Integer roleGroupid;
    /**
     * 组织ID
     */
    public Integer orgid;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public Integer getRoleGroupid() {
	return roleGroupid;
    }

    public void setRoleGroupid(Integer roleGroupid) {
	this.roleGroupid = roleGroupid;
    }

    public Integer getOrgid() {
	return orgid;
    }

    public void setOrgid(Integer orgid) {
	this.orgid = orgid;
    }

}
