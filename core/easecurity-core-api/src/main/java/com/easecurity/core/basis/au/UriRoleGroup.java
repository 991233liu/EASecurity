/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.au;

import com.easecurity.core.basis.au.UriRoleGroupEnum.*;

import java.io.Serializable;

/**
 * au_uri_role_group表：基于大角色的接口（服务）授权
 *
 */
public class UriRoleGroup implements Serializable {

    private static final long serialVersionUID = -2441721451646866788L;

    public Integer id;
    /**
     * URI资源ID
     */
    public Integer uriId;
    /**
     * 大角色ID
     */
    public Integer roleGroupId;
    /**
     * 注解配置信息。如果此配置是来源于FromTo.SOURCECODE，则此值不为空
     */
    public String annotation;
    /**
     * 分组。同一组内多个条件，满足任一条件则此组为true；多组条件，任一组是false，则最终结果为false。 值的范围1~99
     */
    public Integer group1;
    /**
     * 来源，0人工、1程序源代码。如果是来着程序源代码，则只能禁用，不能删除
     */
    public FromTo fromTo;
    /**
     * 状态，0启用、1禁用
     */
    public Status status;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getUriId() {
	return uriId;
    }

    public void setUriId(Integer uriId) {
	this.uriId = uriId;
    }

    public Integer getRoleGroupId() {
	return roleGroupId;
    }

    public void setRoleGroupId(Integer roleGroupId) {
	this.roleGroupId = roleGroupId;
    }

    public String getAnnotation() {
	return annotation;
    }

    public void setAnnotation(String annotation) {
	this.annotation = annotation;
    }

    public Integer getGroup1() {
	return group1;
    }

    public void setGroup1(Integer group1) {
	this.group1 = group1;
    }

    public FromTo getFromTo() {
	return fromTo;
    }

    public void setFromTo(FromTo fromTo) {
	this.fromTo = fromTo;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

}
