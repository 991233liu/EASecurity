/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.b.UserEnum.*;

/**
 * b_user表，用户账号信息
 *
 */
public class User implements Serializable {

    private static final long serialVersionUID = 775751329889302615L;

    public String id;

    /**
     * 登录账号
     */
    public String account;

    /**
     * 密码
     */
    public String pd;

    /**
     * 账号状态
     */
    public AcStatus acStatus;

    /**
     * 密码状态
     */
    public PdStatus pdStatus;
    
    /**
     * 身份串
     */
    public String identities;
    /**
     * 最后登录时间
     */
    public Date lastLoginTtime;
    /**
     * 密码错误次数
     */
    public Integer pdErrorTimes = 0;

    public String getId() { return id; }

    public void setId(String id) {
	this.id = id;
    }

    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }

    public String getPd() {
	return pd;
    }

    public void setPd(String pd) {
	this.pd = pd;
    }

    public AcStatus getAcStatus() { return acStatus; }

    public void setAcStatus(AcStatus acStatus) { this.acStatus = acStatus; }

    public PdStatus getPdStatus() { return pdStatus; }

    public void setPdStatus(PdStatus pdStatus) { this.pdStatus = pdStatus; }

    public String getIdentities() { return identities; }

    public void setIdentities(String identities) {
        this.identities = identities;
    }

    public Date getLastLoginTtime() {
        return lastLoginTtime;
    }

    public void setLastLoginTtime(Date lastLoginTtime) {
        this.lastLoginTtime = lastLoginTtime;
    }

    public Integer getPdErrorTimes() {
        return pdErrorTimes;
    }

    public void setPdErrorTimes(Integer pdErrorTimes) {
        this.pdErrorTimes = pdErrorTimes;
    }
}
