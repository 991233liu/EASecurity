/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.Serializable;
import java.time.Instant;

/**
 * Token
 *
 */
public class Token implements Serializable {

    private static final long serialVersionUID = -412978908635338134L;

    /**
     * Access Token
     */
    public String accessToken;
    /**
     * Refresh Token
     */
    public String refreshToken;
    /**
     * expires
     */
    public Instant expires;
    
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public Instant getExpires() {
        return expires;
    }
    public void setExpires(Instant expires) {
        this.expires = expires;
    }
}
