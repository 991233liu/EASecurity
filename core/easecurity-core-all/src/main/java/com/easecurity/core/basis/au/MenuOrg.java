/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.au;

import java.io.Serializable;

/**
 * au_menu_org表：基于组织的菜单授权
 *
 */
public class MenuOrg implements Serializable {

    private static final long serialVersionUID = 4078667391450742242L;

    public Integer id;
    public Integer menuid;
    public Integer orgid;
    public String fromTo;
    public String status;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getMenuid() {
	return menuid;
    }

    public void setMenuid(Integer menuid) {
	this.menuid = menuid;
    }

    public Integer getOrgid() {
	return orgid;
    }

    public void setOrgid(Integer orgid) {
	this.orgid = orgid;
    }

    public String getFromTo() {
	return fromTo;
    }

    public void setFromTo(String fromTo) {
	this.fromTo = fromTo;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }
}
