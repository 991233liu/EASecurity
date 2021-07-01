/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;

/**
 * user表，用户账号信息
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = 775751329889302615L;

    public String id;

    /**
     * 登录账号
     */
    public String user;

    /**
     * 密码
     */
    public String pd;

    /**
     * 账号状态
     */
    public String uStatus;

    /**
     * 密码状态
     */
    public String pStatus;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getPd() {
	return pd;
    }

    public void setPd(String pd) {
	this.pd = pd;
    }

    public String getuStatus() {
	return uStatus;
    }

    public void setuStatus(String uStatus) {
	this.uStatus = uStatus;
    }

    public String getpStatus() {
	return pStatus;
    }

    public void setpStatus(String pStatus) {
	this.pStatus = pStatus;
    }

}
