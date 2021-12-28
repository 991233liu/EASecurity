/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import com.easecurity.core.basis.b.OrgEnum.Status;
import com.easecurity.core.basis.b.OrgEnum.Type;

import java.io.Serializable;

/**
 * s_gif_captcha表：图片验证码。
 *
 */
public class GifCaptcha implements Serializable {

    private static final long serialVersionUID = 8968703859895801638L;
    
    public Integer id;
    /**
     * SessionID
     */
    public String sessionId;
    /**
     * 验证码key
     */
    public String key2;
    /**
     * 验证码值
     */
    public String value;
    /**
     * 有效期
     */
    public Long validTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }
}
