/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * 验证码错误
 *
 */
public class BadGifCaptchaException extends BadCredentialsException {

    private static final long serialVersionUID = -2942898986889589602L;

    public BadGifCaptchaException(String msg) {
	super(msg);
    }

    public BadGifCaptchaException(String msg, Throwable cause) {
	super(msg, cause);
    }

}
