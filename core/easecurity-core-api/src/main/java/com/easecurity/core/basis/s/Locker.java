/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * s_locker表：业务锁。
 */
public class Locker implements Serializable {

    private static final long serialVersionUID = 6630183838576371456L;

    /**
     * 锁名：唯一标识符
     */
    public String lkey;
    /**
     * 创建时间
     */
    public Date lockTime;
    /**
     * 失效时间间隔
     */
    public Long timeout;
    /**
     * 失效时间
     */
    public Instant timeoutTime;

    public String getLkey() {
	return lkey;
    }

    public void setLkey(String lkey) {
	this.lkey = lkey;
    }

    public Date getLockTime() {
	return lockTime;
    }

    public void setLockTime(Date lockTime) {
	this.lockTime = lockTime;
    }

    public Long getTimeout() {
	return timeout;
    }

    public void setTimeout(Long timeout) {
	this.timeout = timeout;
    }

    public Instant getTimeoutTime() {
	return timeoutTime;
    }

    public void setTimeoutTime(Instant timeoutTime) {
	this.timeoutTime = timeoutTime;
    }
}
