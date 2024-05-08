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
    public String access_token;
    /**
     * Token Type
     */
    public String token_type = "Bearer";
    /**
     * expires
     */
    public Instant expires;
    /**
     * expires
     */
    public long expires_in;
    /**
     * Refresh Token
     */
    public String refresh_token;

}
