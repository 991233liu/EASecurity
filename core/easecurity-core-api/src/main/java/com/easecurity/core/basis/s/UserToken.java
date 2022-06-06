/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.s;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * s_user_token表：用户token。?
 *
 */
public class UserToken implements Serializable {

    private static final long serialVersionUID = 6506441051359837625L;
   
    public Integer id;
    /**
     * 登录账号
     */
    public String account;
    /**
     * Access Token
     */
    public String accessToken;
    /**
     * Access Token expires
     */
    public Instant accessTokenExpires;
    /**
     * Refresh Token
     */
    public String refreshToken;
    /**
     * Refresh Token expires
     */
    public Instant refreshTokenExpires;
    /**
     * UserDetails
     */
    public String userDetails;
    
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
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public Instant getAccessTokenExpires() {
        return accessTokenExpires;
    }
    public void setAccessTokenExpires(Instant accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public Instant getRefreshTokenExpires() {
        return refreshTokenExpires;
    }
    public void setRefreshTokenExpires(Instant refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }
    public String getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
