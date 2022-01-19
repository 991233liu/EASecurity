/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * 
 * JWT信息
 *
 */
public class JWT implements Serializable {

    private static final long serialVersionUID = 8063334560178958374L;

    /**
     * the Issuer claim identifies the principal that issued the JWT
     */
    public String iss;
    /**
     * the Subject claim identifies the principal that is the subject of theJWT
     */
    public String sub;
    /**
     * the Audience claim identifies the recipient(s) that the JWT is intended for
     */
    public String aud;
    /**
     * the Expiration time claim identifies the expiration time on or after which
     * the JWT MUST NOT be accepted for processing
     */
    public Integer exp;
    /**
     * the Not Before claim identifies the time before which the JWT MUST NOT be
     * accepted for processing
     */
    public Integer nbf;
    /**
     * The Issued at claim identifies the time at which the JWT was issued
     */
    public Integer iat;
    /**
     * The JWT ID claim provides a unique identifier for the JWT
     */
    public String jti;
    /**
     * 用户授权信息
     */
    public UserDetails userDetails;

    /**
     * 密文
     */
    public String parsedStr;
    
    public JWT() {
    }

    public JWT(Map<String, Object> map) {
	this.iss = getStringValue(map, "iss");
	this.sub = getStringValue(map, "sub");
	this.aud = getStringValue(map, "aud");
	this.exp = getIntegerValue(map, "exp");
	this.nbf = getIntegerValue(map, "nbf");
	this.iat = getIntegerValue(map, "iat");
	this.jti = getStringValue(map, "jti");
    }

    private String getStringValue(Map<String, Object> map, String key) {
	try {
	    if (map.containsKey(key)) {
		return String.valueOf(map.get(key));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    private Integer getIntegerValue(Map<String, Object> map, String key) {
	try {
	    if (map.containsKey(key)) {
		if (map.get(key) instanceof String) {
		    return Integer.valueOf((String) map.get(key));
		} else {
		    return (Integer) map.get(key);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * JWT是否有效
     */
    public boolean verify() {
	return Instant.now().getEpochSecond() < exp;
    }
    
    /**
     * 删除密文
     * 
     * @return 密文
     */
    public String removeParsedStr() {
	String parsedStr = this.parsedStr;
	this.parsedStr = null;
	return parsedStr;
    }

    public String getIss() {
	return iss;
    }

    public void setIss(String iss) {
	this.iss = iss;
    }

    public String getSub() {
	return sub;
    }

    public void setSub(String sub) {
	this.sub = sub;
    }

    public String getAud() {
	return aud;
    }

    public void setAud(String aud) {
	this.aud = aud;
    }

    public Integer getExp() {
	return exp;
    }

    public void setExp(Integer exp) {
	this.exp = exp;
    }

    public Integer getNbf() {
	return nbf;
    }

    public void setNbf(Integer nbf) {
	this.nbf = nbf;
    }

    public Integer getIat() {
	return iat;
    }

    public void setIat(Integer iat) {
	this.iat = iat;
    }

    public String getJti() {
	return jti;
    }

    public void setJti(String jti) {
	this.jti = jti;
    }

    public UserDetails getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
	this.userDetails = userDetails;
    }

    public String getParsedStr() {
        return parsedStr;
    }

    public void setParsedStr(String parsedStr) {
        this.parsedStr = parsedStr;
    }

}
