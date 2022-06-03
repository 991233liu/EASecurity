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
}
