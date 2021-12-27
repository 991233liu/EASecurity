package com.easecurity.admin.auth

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.springframework.security.web.authentication.WebAuthenticationDetails

import javax.servlet.http.HttpServletRequest

@Canonical
@CompileStatic
class TwoFactorAuthenticationDetails extends WebAuthenticationDetails {
    String gifCaptcha
    String gifCaptchaValue

    TwoFactorAuthenticationDetails(HttpServletRequest request) {
        super(request)
    }
}