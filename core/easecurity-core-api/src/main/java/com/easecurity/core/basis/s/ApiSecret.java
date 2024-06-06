package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.s.ApiSecretEnum.Status;

/**
 * s_api_secret表：api证书表。
 */
public class ApiSecret implements Serializable {

    private static final long serialVersionUID = -3986120590916194947L;
    
    public Integer id;
    /**
     * 渠道id
     */
    public Integer channelId;
    /**
     * 系统id
     */
    public Integer internalSystemId;
    /**
     * key
     */
    public String skey;
    /**
     * secret
     */
    public String secret;
    /**
     * 扩展配置
     */
    public String options;
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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getInternalSystemId() {
        return internalSystemId;
    }

    public void setInternalSystemId(Integer internalSystemId) {
        this.internalSystemId = internalSystemId;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
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
