package com.easecurity.core.authentication.form;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.easecurity.core.basis.s.GifCaptcha;

import javax.servlet.http.HttpServletRequest;

public class TwoFactorAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = -7233994636735471727L;

    public String gifCaptcha;
    public String gifCaptchaValue;
    public GifCaptcha localGifCaptcha1;

    TwoFactorAuthenticationDetails(HttpServletRequest request) {
	super(request);
	localGifCaptcha1 = (GifCaptcha) request.getSession().getAttribute("GifCaptcha");
	request.getSession().removeAttribute("GifCaptcha");
    }
}
