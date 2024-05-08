/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.util.Date;

/**
 * s_server表：服务器实例。
 */
public class Server implements Serializable {

    private static final long serialVersionUID = 6351945265781423978L;

    public Integer id;
    /**
     * 项目实例的id
     */
    public Integer sid;
    /**
     * 项目名称
     */
    public String projetName;
    /**
     * jvm_name
     */
    public String name;
    /**
     * jvm_startTime
     */
    public Long startTime;
    /**
     * ip
     */
    public String ip;
    /**
     * prot
     */
    public String port;
    /**
     * 其它描述
     */
    public String other;
    /**
     * 心跳时间/最后存活时间
     */
    public Date lastTime;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getSid() {
	return sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getProjetName() {
	return projetName;
    }

    public void setProjetName(String projetName) {
	this.projetName = projetName;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Long getStartTime() {
	return startTime;
    }

    public void setStartTime(Long startTime) {
	this.startTime = startTime;
    }

    public String getIp() {
	return ip;
    }

    public void setIp(String ip) {
	this.ip = ip;
    }

    public String getPort() {
	return port;
    }

    public void setPort(String port) {
	this.port = port;
    }

    public String getOther() {
	return other;
    }

    public void setOther(String other) {
	this.other = other;
    }

    public Date getLastTime() {
	return lastTime;
    }

    public void setLastTime(Date lastTime) {
	this.lastTime = lastTime;
    }

}
