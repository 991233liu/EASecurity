package com.easecurity.admin.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.web.authentication.session.SessionAuthenticationException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler

/**
 * 登录失败后消息和跳转处理类。处理逻辑：
 * <p>
 * <ul>
 * <li>如果带有faile_url参数，直接跳转此参数表示的链接；</li>
 * <li>如果没有faile_url参数且为Ajax请求，则返回Ajax消息；</li>
 * <li>其它情况，则按照{@link org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler}处理</li>
 * </ul>
 *
 */
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    MessageSource messageSource

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = ''
        if (exception instanceof AccountExpiredException) {
            msg = messageSource.getMessage('springSecurity.errors.login.expired', null, "Account Expired", request.locale)
        } else if (exception instanceof CredentialsExpiredException) {
            msg = messageSource.getMessage('springSecurity.errors.login.passwordExpired', null, "Password Expired", request.locale)
        } else if (exception instanceof DisabledException) {
            msg = messageSource.getMessage('springSecurity.errors.login.disabled', null, "Account Disabled", request.locale)
        } else if (exception instanceof LockedException) {
            msg = messageSource.getMessage('springSecurity.errors.login.locked', null, "Account Locked", request.locale)
        } else if (exception instanceof SessionAuthenticationException) {
            msg = messageSource.getMessage('springSecurity.errors.login.max.sessions.exceeded', null, "Sorry, you have exceeded your maximum number of open sessions.", request.locale)
        } else if (exception instanceof BadCaptchaException) {
            msg = messageSource.getMessage('springSecurity.errors.login.fail.captcha', null, "Sorry, we were not able to find a user with that captcha", request.locale)
        } else {
            msg = messageSource.getMessage('springSecurity.errors.login.fail', null, "Authentication Failure", request.locale)
        }
        String str = '{"error": "500", "code": "500", "message": "' + msg + '"}'
//        Map map = [code: '200', message: "success"]
        response.setCharacterEncoding("utf-8")
        response.setHeader("Content-Type", "application/json ");
        response.getWriter().write(str)
//        if (springSecurityService.isAjax(request)) {
//            render([error: msg] as JSON)
//        } else {
//            flash.message = msg
//            render view: '/errors/error', model: []
////            render status: HttpServletResponse.SC_INTERNAL_SERVER_ERROR, view: '/errors/error', model: []
//        }
    }
}
