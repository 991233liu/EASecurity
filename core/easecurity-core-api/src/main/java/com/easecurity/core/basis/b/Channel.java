package com.easecurity.core.basis.b;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.b.ChannelEnum.Status;

/**
 * b_channel表：互联网渠道。
 *
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = -4717472228144488906L;
    
    public Integer id;
    /**
     * 渠道唯一标识appid
     */
    public String appid;
    /**
     * 渠道中文名称
     */
    public String cname;
    /**
     * 渠道英文名称
     */
    public String name;
    /**
     * 备注
     */
    public String memo;
    /**
     * 状态
     */
    public Status status;

    public Date dateCreated;
    public Date lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
