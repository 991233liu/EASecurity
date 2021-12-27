package com.easecurity.admin.auth

import groovy.transform.CompileStatic
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        System.out.println("-------# a1")
        // TODO 密码动态处理，目前domain中写死的
        // 校验图片动态验证码
        Object details = authentication.details
        if (!(details instanceof TwoFactorAuthenticationDetails)) {
            logger.debug("Authentication failed: authenticationToken principal is not a TwoFactorPrincipal");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        def twoFactorAuthenticationDetails = details as TwoFactorAuthenticationDetails
        System.out.println("-------# 输入的验证码key为：" + twoFactorAuthenticationDetails.gifCaptcha)
        System.out.println("-------# 输入的验证码value为：" + twoFactorAuthenticationDetails.gifCaptchaValue)
//        if ( !coordinateValidator.isValidValueForPositionAndUserName(twoFactorAuthenticationDetails.coordinateValue, twoFactorAuthenticationDetails.coordinatePosition, authentication.name) ) {
//            logger.debug("Authentication failed: gifCaptcha note valid");
//            throw new BadCredentialsException(messages.getMessage(
//                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
//                    "Bad credentials"));
//        }

        // 校验密码
        System.out.println("-------# 1=" + userDetails.username + userDetails.password)
        System.out.println("-------# 1=" + userDetails)
        System.out.println("-------# 1=" + authentication.name + authentication.properties)
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!userDetails.getPassword().equals(presentedPassword)) {
            logger.debug("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        System.out.println("-------# a5")
    }
}
