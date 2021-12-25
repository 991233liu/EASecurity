/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;

/**
 * b_user_info表：用户信息
 *
 */
public class UserInfo implements Serializable {

    private static final long serialVersionuserId = 5223521401840975133L;

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
    public String status;

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

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

}
