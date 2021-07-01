/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

/**
 * b_org表：组织。
 *
 */
public class Org {

    public Integer id;
    public String name;
    public String code;
    public String type;
    public String status;
    public Integer parentid;
    public String fullName;
    public String fullPathid;
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

    public Integer getParentid() {
	return parentid;
    }

    public void setParentid(Integer parentid) {
	this.parentid = parentid;
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
