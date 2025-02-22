/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.b.OrgEnum.*;

/**
 * b_org表：组织。
 *
 */
public class Org implements Serializable {

    private static final long serialVersionUID = 8968703859895801527L;
    
    public Integer id;
    /**
     * 组织名称
     */
    public String name;
    /**
     * 组织编码（业务用）
     */
    public String code;
    /**
     * 类型:0根、1机构、2公司、3办事处、4部门、5临时
     */
    public Type type;
    /**
     * 组织状态
     */
    public Status status;
    /**
     * 父节点ID
     */
    public Integer parentId;
    /**
     * 全路径名称，如：/xx公司/xx部门/
     */
    public String fullName;
    /**
     * 全路径ID，如：/0/123/456/
     */
    public String fullPathid;
    /**
     * 全路径组织编码，如：/0/abc/def/
     */
    public String fullCode;

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

    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public Integer getParentId() {
	return parentId;
    }

    public void setParentId(Integer parentId) {
	this.parentId = parentId;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public String getFullPathid() {
	return fullPathid;
    }

    public void setFullPathid(String fullPathid) {
	this.fullPathid = fullPathid;
    }

    public String getFullCode() {
	return fullCode;
    }

    public void setFullCode(String fullCode) {
	this.fullCode = fullCode;
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
