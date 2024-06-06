package com.easecurity.admin.auth

import groovy.transform.CompileStatic
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

import javax.servlet.http.HttpServletRequest

@CompileStatic
class TwoFactorAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    @Override
    WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        TwoFactorAuthenticationDetails details = new TwoFactorAuthenticationDetails(context)
        details.captcha = context.getParameter('captcha')
        details.captchaKey = context.getParameter('captchaKey')
        details
    }

}
