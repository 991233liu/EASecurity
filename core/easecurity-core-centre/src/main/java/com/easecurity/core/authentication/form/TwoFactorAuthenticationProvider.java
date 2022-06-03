package com.easecurity.core.authentication.form;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
    private LoginService loginService;

    @Value("${loginCaptcha.disable:true}")
    private boolean loginCaptchaDisable;

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
	// 校验图片动态验证码
	String gifCaptcha = ServletUtils.getRequest().getParameter("gifCaptcha");
	String gifCaptchaValue = ServletUtils.getRequest().getParameter("gifCaptchaValue");
	if (logger.isDebugEnabled()) {
	    logger.debug("-------# 输入的验证码key为：" + gifCaptcha);
	    logger.debug("-------# 输入的验证码value为：" + gifCaptchaValue);
	}
	// TODO 如果是跨域登录，且没有使用EASecurity的登录页面，则session无效。此时提交登录包含key和输入的吗
	HttpSession session = ServletUtils.getSession();
	GifCaptcha gifCaptcha1 = session == null ? null : (GifCaptcha) session.getAttribute("GifCaptcha");
//	System.out.println("-------# 本地的验证码value为：" + gifCaptcha1.value);
	// TODO 数据库验证
	// TODO Redis验证
	if (!loginCaptchaDisable && ((gifCaptcha1 == null) || !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.value.equals(gifCaptchaValue.toLowerCase())))) {
	    logger.debug("Authentication failed: gifCaptcha note valid");
	    throw new BadGifCaptchaException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badGifCaptcha", "Bad gifCaptcha"));
	}
	ServletUtils.getSession().removeAttribute("GifCaptcha");

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
