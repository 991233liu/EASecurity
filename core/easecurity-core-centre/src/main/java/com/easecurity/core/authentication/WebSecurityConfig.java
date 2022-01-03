/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @Description: spring security配置类
 *
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

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
        	    "/", "/auth/**", "/css/**", "/js/**", "/images/**", "/fonts/**")
            .permitAll()
            .antMatchers("/**")
            .authenticated()
            .and()
        .formLogin()
            .loginPage("/auth/login")
            .loginProcessingUrl("/login")
            .permitAll()
//            .defaultSuccessUrl("/todo.html", true)
            .authenticationDetailsSource(authenticationDetailsSource)
        .and()
            .logout()
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
            .and()
        .csrf().disable();
    }
}
