/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.r;

import java.io.Serializable;
import java.util.Date;

/**
 * r_role_group表：大角色信息。
 *
 */
public class RoleGroup implements Serializable {

    private static final long serialVersionUID = -2524437357623582359L;

    public Integer id;
    /**
     * 角色名称
     */
    public String name;
    /**
     * 角色编码
     */
    public String code;

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

    public String getCode() { return code; }

    public void setCode(String code) {
	this.code = code;
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
