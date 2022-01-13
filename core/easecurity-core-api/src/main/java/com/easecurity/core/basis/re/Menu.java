/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis.re;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.re.MenuEnum.*;

/**
 * re_menu表：菜单
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 5047002819489149714L;

    public Integer id;

    /**
     * 菜单名称
     */
    public String name;
    /**
     * 菜单编码
     */
    public String code;
    /**
     * 菜单级别
     */
    public Level level;
    /**
     * 显示顺序
     */
    public Integer sortNumber;
    /**
     * 打开链接URL
     */
    public String openUrl;
    /**
     * 图标
     */
    public String icon;
    /**
     * 上级菜单ID
     */
    public Integer parentId;
    /**
     * 全路径名称，如：默认菜单/系统管理/用户管理/
     */
    public String fullName;
    /**
     * 状态，0启用，1禁用
     */
    public Status status;
    /**
     * 显示状态，0始终隐藏、1始终显示、2禁用隐藏、3、无权限隐藏
     */
    public DisplayStatus displayStatus = DisplayStatus.NOPERMISSIONSHIDDEN;
    /**
     * 禁用提示
     */
    public String disablePrompt;
    /**
     * 无权限提示
     */
    public String noPermissionsPrompt;
    /**
     * 访问类型，0匿名访问、1登录用户访问、2授权访问
     */
    public AccessType accessType = AccessType.LOGIN;

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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DisplayStatus getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(DisplayStatus displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisablePrompt() {
        return disablePrompt;
    }

    public void setDisablePrompt(String disablePrompt) {
        this.disablePrompt = disablePrompt;
    }

    public String getNoPermissionsPrompt() {
        return noPermissionsPrompt;
    }

    public void setNoPermissionsPrompt(String noPermissionsPrompt) {
        this.noPermissionsPrompt = noPermissionsPrompt;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
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
