/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.re;

import java.io.Serializable;

/**
 * menu表：菜单
 *
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 5047002819489149714L;

    public Integer id;

    /**
     * 菜单名称
     */
    public String name;
    public String code;
    public Integer level;
    public Integer sort;
    public Integer parentid;
    public String openUrl;
    public String icon;
    public String status;
    public String displayStatus;
    public String disablePrompt;
    public String noPermissionsPprompt;
    public String accessType;

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

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public Integer getSort() {
	return sort;
    }

    public void setSort(Integer sort) {
	this.sort = sort;
    }

    public Integer getParentid() {
	return parentid;
    }

    public void setParentid(Integer parentid) {
	this.parentid = parentid;
    }

    public String getOpenUrl() {
	return openUrl;
    }

    public void setOpenUrl(String openUrl) {
	this.openUrl = openUrl;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getDisplayStatus() {
	return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
	this.displayStatus = displayStatus;
    }

    public String getDisablePrompt() {
	return disablePrompt;
    }

    public void setDisablePrompt(String disablePrompt) {
	this.disablePrompt = disablePrompt;
    }

    public String getNoPermissionsPprompt() {
	return noPermissionsPprompt;
    }

    public void setNoPermissionsPprompt(String noPermissionsPprompt) {
	this.noPermissionsPprompt = noPermissionsPprompt;
    }

    public String getAccessType() {
	return accessType;
    }

    public void setAccessType(String accessType) {
	this.accessType = accessType;
    }

}
