//import com.easecurity.admin.auth.UserPasswordEncoderListener
import com.easecurity.admin.auth.CustomUserDetailsService
import com.easecurity.admin.auth.TwoFactorAuthenticationDetailsSource
import com.easecurity.admin.auth.TwoFactorAuthenticationProvider

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
}

