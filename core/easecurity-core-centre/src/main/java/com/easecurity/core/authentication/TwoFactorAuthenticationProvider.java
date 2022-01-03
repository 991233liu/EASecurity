package com.easecurity.core.authentication;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.s.GifCaptcha;

//@CompileStatic
@Service
public class TwoFactorAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    public UserDetailsService userDetailsService;
    @Autowired
    LoginService loginService;

    @Value("${loginCaptcha.disable:true}")
    boolean loginCaptchaDisable;

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
	System.out.println("-------# a1");
	// 校验图片动态验证码
	Object details = authentication.getDetails();
	if (!(details instanceof TwoFactorAuthenticationDetails)) {
	    logger.debug("Authentication failed: authenticationToken principal is not a TwoFactorPrincipal");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}
	TwoFactorAuthenticationDetails twoFactorAuthenticationDetails = (TwoFactorAuthenticationDetails) details;
	System.out.println("-------# 输入的验证码key为：" + twoFactorAuthenticationDetails.gifCaptcha);
	System.out.println("-------# 输入的验证码value为：" + twoFactorAuthenticationDetails.gifCaptchaValue);
	GifCaptcha gifCaptcha1 = twoFactorAuthenticationDetails.localGifCaptcha1;
	System.out.println("-------# 本地的验证码value为：" + gifCaptcha1.value);
	// TODO 数据库验证
	// TODO Redis验证
	if (!loginCaptchaDisable
		&& !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.value.equals(twoFactorAuthenticationDetails.gifCaptchaValue.toLowerCase()))) {
	    logger.debug("Authentication failed: gifCaptcha note valid");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}
	twoFactorAuthenticationDetails.localGifCaptcha1 = null;

	// 校验密码
	System.out.println("-------# 1=" + userDetails.getUsername() + userDetails.getPassword());
//        System.out.println("-------# 1=" + userDetails.properties);
	System.out.println("-------# 1=" + authentication.getName());
	if (authentication.getCredentials() == null) {
	    logger.debug("Authentication failed: no credentials provided");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	String presentedPassword = authentication.getCredentials().toString();
	System.out.println("-------# 1=" + presentedPassword);

	if (!customUserDetails.getPassword().equals(presentedPassword)) {
	    loginService.loginFail(customUserDetails);
	    logger.debug("Authentication failed: password does not match stored value");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	loginService.loginSuccess(customUserDetails);
	System.out.println("-------# a5");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	// TODO Auto-generated method stub
	UserDetails loadedUser = userDetailsService.loadUserByUsername(username);
	return loadedUser;
    }
}
