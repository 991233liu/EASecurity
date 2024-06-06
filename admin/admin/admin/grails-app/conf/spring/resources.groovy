import grails.plugin.springsecurity.*
import com.easecurity.admin.auth.*

// Place your Spring DSL code here
beans = {
//    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)

    // 双因子登录：密码+图片验证码
    authenticationDetailsSource(TwoFactorAuthenticationDetailsSource)
    twoFactorAuthenticationProvider(TwoFactorAuthenticationProvider) {
        userDetailsService = ref('userDetailsService')
        passwordEncoder = ref('passwordEncoder')
        userCache = ref('userCache')
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        authoritiesMapper = ref('authoritiesMapper')
        hideUserNotFoundExceptions = true
    }

    // 登录成功SuccessHandler
    authenticationSuccessHandler(LoginSuccessHandler) {
        /* Reusing the security configuration */
        def conf = SpringSecurityUtils.securityConfig
        /* Configuring the bean */
        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
        defaultTargetUrl = conf.successHandler.defaultTargetUrl
        alwaysUseDefaultTargetUrl = conf.successHandler.alwaysUseDefault
        targetUrlParameter = conf.successHandler.targetUrlParameter
        useReferer = conf.successHandler.useReferer
    }

    // 登录失败FailureHandler
    authenticationFailureHandler(LoginFailureHandler) {
        /* Reusing the security configuration */
        def conf = SpringSecurityUtils.securityConfig
        /* Configuring the bean */
//        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
        defaultFailureUrl = conf.failureHandler.defaultFailureUrl
    }
}

