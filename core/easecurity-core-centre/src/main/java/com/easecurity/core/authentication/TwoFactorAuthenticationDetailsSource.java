package com.easecurity.core.authentication;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("authenticationDetailsSource")
public class TwoFactorAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        TwoFactorAuthenticationDetails details = new TwoFactorAuthenticationDetails(context);
        details.gifCaptcha = context.getParameter("gifCaptcha");
        details.gifCaptchaValue = context.getParameter("gifCaptchaValue");
        return details;
    }

}
