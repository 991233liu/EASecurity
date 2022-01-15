/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.easecurity.core.authentication.form.CookieLogoutHandler;
import com.easecurity.core.authentication.form.LoginFailureHandler;
import com.easecurity.core.authentication.form.LoginSuccessHandler;

/**
 * @Description: spring security配置类
 *
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CookieLogoutHandler cookieLogoutHandler;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//	super.configure(http);
	http
//        .headers()
//            .cacheControl()
//            .contentTypeOptions()
//            .httpStrictTransportSecurity()
//            .xssProtection()
//            .and()
        .authorizeRequests()
            .antMatchers(
        	    "/", "/auth/**", "/css/**", "/js/**", "/images/**", "/fonts/**",
        	    "/data/**")
            .permitAll()
            .antMatchers("/**")
            .authenticated()
            .and()
        .formLogin()
            .loginPage("/auth/login")
            .loginProcessingUrl("/login")
            .permitAll()
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
//            .failureForwardUrl("/auth/login?aa=aa")
//            .failureUrl("/auth/login?aa=aa")
//            .authenticationDetailsSource(authenticationDetailsSource)
        .and()
            .logout()
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/auth/login?logout")
            .addLogoutHandler(cookieLogoutHandler)
        .and()
            .sessionManagement()
            .maximumSessions(1)
//            .maxSessionsPreventsLogin(true)	// 当session达到最大有效数的时候，不再允许相同的账户登录。
//            .expiredSessionStrategy(null)	// 当session过期时的处理
            .and()
	// TODO CSRF攻击被关闭了。如何防御？？？
        .and()
            .csrf().disable();
    }
}
