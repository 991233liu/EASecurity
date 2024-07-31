/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.easecurity.core.authentication.form.LogoutHandler;
import com.easecurity.core.authentication.form.CustomConcurrentSessionControlAuthenticationStrategy;
import com.easecurity.core.authentication.form.LoginFailureHandler;
import com.easecurity.core.authentication.form.LoginSuccessHandler;

/**
 * @Description: spring security配置类
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private LogoutHandler logoutHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Value("${server.servlet.session.cookie.name:JSESSIONID}")
    private String cookieName;
    @Value("${easecurity.jwt.token.issuer}")
    private String issuer;
    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI)) //
                .oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
//            .addFilterBefore(webSecurityFilter, SecurityContextHolderFilter.class)
//            .addFilterAfter(webSecurityRegisteredUserFilter, SecurityContextHolderFilter.class)
                .requestMatcher(endpointsMatcher).authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt).apply(authorizationServerConfigurer);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/auth/**", "/jwt/**", "/token/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/data/**").permitAll() //
                .antMatchers("/**").authenticated() //
                .and() //
                .formLogin().loginPage("/auth/login").loginProcessingUrl("/login").permitAll() //
                .successHandler(loginSuccessHandler) //
                .failureHandler(loginFailureHandler) //
//          .failureForwardUrl("/auth/login?aa=aa")
//          .failureUrl("/auth/login?aa=aa")
//          .authenticationDetailsSource(authenticationDetailsSource)
                .and() //
                .logout().logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/auth/login?logout") //
                .addLogoutHandler(logoutHandler) //
                .and() //
                .sessionManagement().maximumSessions(1);
//          .maxSessionsPreventsLogin(true)   // 当session达到最大有效数的时候，不再允许相同的账户登录。
//          .expiredSessionStrategy(null) // 当session过期时的处理

        http.oauth2ResourceServer().jwt();

        http.csrf(csrf -> csrf.ignoringAntMatchers("/data/**"));
//        http.csrf().disable();

        // 自定义的并发控制器
        http.formLogin().and().sessionManagement().addObjectPostProcessor(new ObjectPostProcessor<ConcurrentSessionControlAuthenticationStrategy>() {

            @Override
            @SuppressWarnings("unchecked")
            public ConcurrentSessionControlAuthenticationStrategy postProcess(ConcurrentSessionControlAuthenticationStrategy object) {
                return getConcurrentSessionControlAuthenticationStrategy(object);
            }

        });
        return http.build();
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
            custom.setCookieName(cookieName);
            return custom;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(this.issuer).build();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        // Will be used by the ConsentController
        // TODO 现在是内存
        return new InMemoryOAuth2AuthorizationConsentService();
    }
}
