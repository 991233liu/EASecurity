package com.easecurity.core.authentication.form;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.b.UserEnum;
import com.easecurity.core.basis.s.GifCaptcha;
import com.easecurity.core.utils.CacheUtil;
import com.easecurity.core.utils.ServletUtils;

@Service
public class TwoFactorAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @Value("${loginCaptcha.disable:true}")
    private boolean loginCaptchaDisable;

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        SecurityCentreUserDetails customUserDetails = (SecurityCentreUserDetails) userDetails;
        // 校验图片动态验证码
        if (!loginCaptchaDisable) {
            String gifCaptcha = ServletUtils.getRequest().getParameter("gifCaptcha");
            String gifCaptchaValue = ServletUtils.getRequest().getParameter("gifCaptchaValue");
            if (logger.isDebugEnabled()) {
                logger.debug("-------# 输入的验证码key为：" + gifCaptcha);
                logger.debug("-------# 输入的验证码value为：" + gifCaptchaValue);
            }
            GifCaptcha gifCaptcha1 = (GifCaptcha) CacheUtil.getCache("GifCaptcha:" + gifCaptcha);
//	System.out.println("-------# 本地的验证码value为：" + gifCaptcha1.value);
            // TODO 数据库验证
            if ((gifCaptcha1 == null) || !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.gvalue.equals(gifCaptchaValue.toLowerCase()))) {
                logger.debug("Authentication failed: gifCaptcha note valid");
                throw new BadGifCaptchaException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badGifCaptcha", "Bad gifCaptcha"));
            }
            CacheUtil.delCache("GifCaptcha:" + gifCaptcha);
        }

        // 校验密码
        if (logger.isDebugEnabled())
            logger.debug("-------# 1=" + userDetails.getUsername() + userDetails.getPassword());
        if (authentication.getCredentials() == null) {
            loginService.loginFail(customUserDetails);
            logger.debug("Authentication failed: no credentials provided1");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();
        if (presentedPassword.length() < 60) {
            loginService.loginFail(customUserDetails);
            logger.debug("Authentication failed: no credentials provided2");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        presentedPassword = presentedPassword.substring(presentedPassword.length() - 31);
        if (!new BCryptPasswordEncoder().matches(presentedPassword, customUserDetails.getPassword())) {
            loginService.loginFail(customUserDetails);
            logger.debug("Authentication failed: password does not match stored value3");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        loginService.loginSuccess(customUserDetails);
    }

    /*
     * 解决共享session模式下，远程应用无自定义用户类的问题
     */
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        if (principal instanceof SecurityCentreUserDetails) {
            SecurityCentreUserDetails userDetails = (SecurityCentreUserDetails) principal;
            principal = SecurityCentreUserDetails.withUserDetails(userDetails).build();
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser = loadUserByUsername(username);
        return loadedUser;
    }

    // TODO 登录性能待优化
    private UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo user = userService.getUserDoForLogin(username);
        if (user == null)
            throw new UsernameNotFoundException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));

        // or if you are using role groups:
        // def roles = user.authorities.collect { it.authorities }.flatten().unique()

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        user.roleUsers.forEach(it -> {
            authorities.add(new SimpleGrantedAuthority(it.roleCode));
        });

//	return new User(username, user.user.pd, user.user.acStatus == UserEnum.AcStatus.ENABLED, !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED),
//		!(user.user.pdStatus == UserEnum.PdStatus.EXPIRED), user.user.pdStatus == UserEnum.PdStatus.ENABLED, authorities);
        return new SecurityCentreUserDetails(username, user.user.pd, user.user.acStatus == UserEnum.AcStatus.ENABLED, !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED),
                !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED), user.user.pdStatus == UserEnum.PdStatus.ENABLED, authorities, user.user.id, user.userinfo.name,
                user.userinfo.avatar, user.user.identities, user.user.lastLoginTtime, user.user.pdErrorTimes);
    }
}
