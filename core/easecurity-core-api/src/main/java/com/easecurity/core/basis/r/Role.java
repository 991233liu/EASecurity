/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis.r;

import java.io.Serializable;
import java.util.Date;


/**
 * r_role表：小角色信息。
 *
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -5985962922034933727L;

    /**
     * 小角色主键
     */
    public Integer id;
    /**
     * 角色名称
     */
    public String name;
    /**
     * 小角色编码=组织编码+'#'+大角色编码
     */
    public String code;
    /**
     * 小角色全称=组织名称+角色名称
     */
    public String fullName;
    /**
     * 大角色ID
     */
    public Integer roleGroupId;
    /**
     * 组织ID
     */
    public Integer orgId;

    public Date dateCreated;
    public Date lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getRoleGroupId() {
        return roleGroupId;
    }

    public void setRoleGroupId(Integer roleGroupId) {
        this.roleGroupId = roleGroupId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
