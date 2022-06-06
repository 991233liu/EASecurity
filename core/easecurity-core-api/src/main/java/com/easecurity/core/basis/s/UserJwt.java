/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.util.Date;

/**
 * s_user_jwt表：用户jwt。
 * // TODO 存库？先存Redis吧，存库似乎没有意义
 *
 */
public class UserJwt implements Serializable {

    private static final long serialVersionUID = -4297763097973720267L;
    
    public Integer id;
    /**
     * 登录账号
     */
    public String account;
    /**
     * Access Token / uuid
     */
    public String jti;
    /**
     * expires
     */
    public Date expires;
    /**
     * jwt
     */
    public String jwt;
    
    public Date dateCreated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
