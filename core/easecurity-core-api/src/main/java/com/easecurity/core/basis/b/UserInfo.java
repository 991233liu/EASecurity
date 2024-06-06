/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis.b;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.b.UserInfoEnum.*;

/**
 * b_user_info表：用户信息
 *
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -938014939971884433L;

    public String id;
    /**
     * user表主键
     */
    public String userId;
    /**
     * 登录账号
     */
    public String account;
    /**
     * 姓名
     */
    public String name;
    /**
     * 用户状态
     */
    public Status status = Status.ENABLED;
    /**
     * 用户图标
     */
    public String avatar;
    /**
     * email
     */
    public String email;
    /**
     * 电话
     */
    public String phone;

    public Date dateCreated;
    public Date lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
