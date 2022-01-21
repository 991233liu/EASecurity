/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.lang.reflect.Field;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.easecurity.core.authentication.form.CookieLogoutHandler;
import com.easecurity.core.authentication.form.CustomConcurrentSessionControlAuthenticationStrategy;
import com.easecurity.core.authentication.form.LoginFailureHandler;
import com.easecurity.core.authentication.form.LoginSuccessHandler;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

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
    
    @Value("${easecurity.jwt.publicKey}")
    RSAPublicKey key;
    @Value("${easecurity.jwt.privateKey}")
    RSAPrivateKey priv;
    @Value("${easecurity.client.logout.url:}")
    private List<String> logoutUrls;

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
	
	//  自定义的并发控制器
	http.formLogin().and()
          .sessionManagement()
          .addObjectPostProcessor(new ObjectPostProcessor<ConcurrentSessionControlAuthenticationStrategy>() {

		@Override
		@SuppressWarnings("unchecked")
		public ConcurrentSessionControlAuthenticationStrategy postProcess(ConcurrentSessionControlAuthenticationStrategy object) {
		    return getConcurrentSessionControlAuthenticationStrategy(object);
		}
      	
          });
    }
    
    /**
     * 自定义的并发控制器
     */
    private ConcurrentSessionControlAuthenticationStrategy getConcurrentSessionControlAuthenticationStrategy(ConcurrentSessionControlAuthenticationStrategy object) {
	try {
	    Field field = object.getClass().getDeclaredField("sessionRegistry");
	    field.setAccessible(true);
	    CustomConcurrentSessionControlAuthenticationStrategy custom = new CustomConcurrentSessionControlAuthenticationStrategy((SessionRegistry) field.get(object));
	    field = object.getClass().getDeclaredField("messages");
	    field.setAccessible(true);
	    custom.setMessageSource((MessageSourceAccessor) field.get(object));
	    field = object.getClass().getDeclaredField("exceptionIfMaximumExceeded");
	    field.setAccessible(true);
	    custom.setExceptionIfMaximumExceeded((boolean) field.get(object));
	    field = object.getClass().getDeclaredField("maximumSessions");
	    field.setAccessible(true);
	    custom.setMaximumSessions((int) field.get(object));
	    custom.setLogoutUrls(logoutUrls);
	    return custom;
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    @Bean
    JwtDecoder jwtDecoder() {
	return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
	JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
	JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
	return new NimbusJwtEncoder(jwks);
    }
}
