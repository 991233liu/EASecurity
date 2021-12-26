/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;

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
     * 类型:0根、1机构、2公司、3办事处、4部门
     */
    public String type;
    /**
     * 组织状态
     */
    public String status;
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

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
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

}
