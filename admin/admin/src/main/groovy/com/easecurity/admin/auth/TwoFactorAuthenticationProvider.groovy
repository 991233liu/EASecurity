package com.easecurity.admin.auth

import com.easecurity.admin.core.b.User
import com.easecurity.admin.utils.ServletUtils
import com.easecurity.core.basis.b.UserEnum
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

//@CompileStatic
class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {
    @Value('${loginCaptcha.disable:true}')
    boolean loginCaptchaDisable

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        CustomUserDetails customUserDetails = userDetails as CustomUserDetails
        System.out.println("-------# a1")
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
        GifCaptcha gifCaptcha1 = (GifCaptcha) ServletUtils.getSession()?.getAttribute("GifCaptcha")
        ServletUtils.getSession().removeAttribute("GifCaptcha")
        System.out.println("-------# 本地的验证码value为：" + gifCaptcha1?.gvalue)
        // TODO 数据库验证
        // TODO Redis验证
        if (!loginCaptchaDisable && (gifCaptcha1 == null || !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.gvalue.equals(twoFactorAuthenticationDetails.gifCaptchaValue.toLowerCase())))) {
            logger.debug("Authentication failed: gifCaptcha note valid");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        // 校验密码
        System.out.println("-------# 1=" + userDetails.username + userDetails.password)
        System.out.println("-------# 1=" + userDetails.properties)
        System.out.println("-------# 1=" + authentication.name + authentication.properties)
        if (authentication.getCredentials() == null) {
            loginFail(customUserDetails.username)
            logger.debug("Authentication failed: no credentials provided1")
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
        }

        String presentedPassword = authentication.getCredentials().toString()
        System.out.println("-------# 1=" + presentedPassword)
        if (presentedPassword.length() < 60) {
            loginFail(customUserDetails.username);
            logger.debug("Authentication failed: no credentials provided2")
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
        }
        presentedPassword = presentedPassword.substring(presentedPassword.length() - 31)
        System.out.println("-------# 1=" + presentedPassword)
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(5)
//        System.out.println("-------# 1=" + bcryptPasswordEncoder.encode(presentedPassword))
//	System.out.println("-------# 1=" + bcryptPasswordEncoder.matches(presentedPassword, customUserDetails.getPassword()))

        if (!new BCryptPasswordEncoder().matches(presentedPassword, customUserDetails.getPassword())) {
            loginFail(customUserDetails.username)
            logger.debug("Authentication failed: password does not match stored value3")
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
        }

        loginSuccess(customUserDetails.username)
        System.out.println("-------# a5")
    }

    /*
     * 密码校验失败
     */

    private void loginFail(String account) {
        User.withTransaction {
            User user1 = User.findByAccount(account)
            if (user1.pdErrorTimes != null) user1.pdErrorTimes += 1
            else user1.pdErrorTimes = 1
            if (user1.pdErrorTimes >= 5) user1.pdStatus = UserEnum.PdStatus.MAXTIMES
            user1.save()
        }
    }

    /*
     * 成功登录
     */

    private void loginSuccess(String account) {
        User.withTransaction {
            User user1 = User.findByAccount(account)
            user1.pdErrorTimes = 0
            user1.lastLoginTtime = new Date()
            user1.save()
        }
    }
}
