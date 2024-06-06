package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.util.Date;

import com.easecurity.core.basis.s.CertificatesEnum.*;

/**
 * s_certificates表：数字证书/秘钥表。
 */
public class Certificates implements Serializable {

    private static final long serialVersionUID = 2222408229214616651L;
    
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
     * 证书唯一Id
     */
    public String keyId;
    /**
     * 公钥/秘钥1
     */
    public String publicKey;
    /**
     * 私钥/秘钥2
     */
    public String privateKey;
    /**
     * 扩展配置
     */
    public String options;
    /**
     * 类型
     */
    public Type type;
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

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
