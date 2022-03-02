package com.easecurity.core.authentication.form;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.basis.s.GifCaptcha;
import com.easecurity.core.utils.ServletUtils;

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
//	Object details = authentication.getDetails();
//	if (!(details instanceof TwoFactorAuthenticationDetails)) {
//	    logger.debug("Authentication failed: authenticationToken principal is not a TwoFactorPrincipal");
//	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//	}
//	TwoFactorAuthenticationDetails twoFactorAuthenticationDetails = (TwoFactorAuthenticationDetails) details;
	String gifCaptcha = ServletUtils.getRequest().getParameter("gifCaptcha");
	String gifCaptchaValue = ServletUtils.getRequest().getParameter("gifCaptchaValue");
	System.out.println("-------# 输入的验证码key为：" + gifCaptcha);
	System.out.println("-------# 输入的验证码value为：" + gifCaptchaValue);
	GifCaptcha gifCaptcha1 = (GifCaptcha) ServletUtils.getSession().getAttribute("GifCaptcha");
//	System.out.println("-------# 本地的验证码value为：" + gifCaptcha1.value);
	// TODO 数据库验证
	// TODO Redis验证
	if (!loginCaptchaDisable && ((gifCaptcha1 == null) || !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.value.equals(gifCaptchaValue.toLowerCase())))) {
	    logger.debug("Authentication failed: gifCaptcha note valid");
	    throw new BadGifCaptchaException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badGifCaptcha", "Bad gifCaptcha"));
	}
	ServletUtils.getSession().removeAttribute("GifCaptcha");

	// 校验密码
	System.out.println("-------# 1=" + userDetails.getUsername() + userDetails.getPassword());
//        System.out.println("-------# 1=" + userDetails.properties);
	System.out.println("-------# 1=" + authentication.getName());
	if (authentication.getCredentials() == null) {
	    loginService.loginFail(customUserDetails);
	    logger.debug("Authentication failed: no credentials provided1");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	String presentedPassword = authentication.getCredentials().toString();
	System.out.println("-------# 1=" + presentedPassword);
	if (presentedPassword.length() < 60) {
	    loginService.loginFail(customUserDetails);
	    logger.debug("Authentication failed: no credentials provided2");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}
	presentedPassword = presentedPassword.substring(presentedPassword.length() - 31);
	System.out.println("-------# 1=" + presentedPassword);
	BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(5);
	System.out.println("-------# 1=" + bcryptPasswordEncoder.encode(presentedPassword));
//	String customUser = customUserDetails.getPassword();
//	String userSalt = customUser.substring(0, customUser.length() - 31);
//	String userPassword = customUser.substring(customUser.length() - 31);
//	System.out.println("-------# 1=" + userSalt);
//	System.out.println("-------# 1=" + bcryptPasswordEncoder.matches(presentedPassword, customUserDetails.getPassword()));

	if (!new BCryptPasswordEncoder().matches(presentedPassword, customUserDetails.getPassword())) {
	    loginService.loginFail(customUserDetails);
	    logger.debug("Authentication failed: password does not match stored value3");
	    throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	loginService.loginSuccess(customUserDetails);
	System.out.println("-------# a5");
    }

    /*
     * 解决共享session模式下，远程应用无自定义用户类的问题
     */
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
	if (principal instanceof CustomUserDetails) {
	    CustomUserDetails customUserDetails = (CustomUserDetails) principal;
	    principal = CustomUserDetails.withUserDetails(customUserDetails).build();
	}
	return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	UserDetails loadedUser = userDetailsService.loadUserByUsername(username);
	return loadedUser;
    }
}
