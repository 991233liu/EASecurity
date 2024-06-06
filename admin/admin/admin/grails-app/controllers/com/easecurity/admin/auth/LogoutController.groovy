package com.easecurity.admin.auth

import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured

import javax.servlet.http.HttpServletResponse

@Secured('permitAll')
class LogoutController extends grails.plugin.springsecurity.LogoutController {

    /**
     * Index action. Redirects to the Spring security logout uri.
     */
    def index() {

        if (!request.post && SpringSecurityUtils.getSecurityConfig().logout.postOnly) {
            response.sendError HttpServletResponse.SC_METHOD_NOT_ALLOWED // 405
            return
        }

        Map map = [code: '200', message: "success"]
        respond map, model: []
    }
}