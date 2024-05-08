/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.util.Date;

/**
 * s_gif_captcha表：图片验证码。
 *
 */
public class GifCaptcha implements Serializable {

    private static final long serialVersionUID = 8968703859895801638L;

    public Integer id;
    /**
     * 验证码key
     */
    public String gkey;
    /**
     * 验证码值
     */
    public String gvalue;
    /**
     * 有效期
     */
    public Long validTime;
    
    public Date dateCreated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGkey() {
        return gkey;
    }

    public void setGkey(String gkey) {
        this.gkey = gkey;
    }

    public String getGvalue() {
        return gvalue;
    }

    public void setGvalue(String gvalue) {
        this.gvalue = gvalue;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

}
