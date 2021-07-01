/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

/**
 * user表，用户账号信息
 *
 */
public class User {

	String id;
	String user;
	String pd;
	String uStatus;
	String pStatus;

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
