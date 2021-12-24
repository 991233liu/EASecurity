//import com.easecurity.admin.auth.UserPasswordEncoderListener
import com.easecurity.admin.auth.CustomUserDetailsService
import com.easecurity.admin.auth.CoordinateValidatorService
import com.easecurity.admin.auth.TwoFactorAuthenticationDetailsSource
import com.easecurity.admin.auth.TwoFactorAuthenticationProvider

// Place your Spring DSL code here
beans = {
//    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)

// tag::authenticationDetailsSource[]
    authenticationDetailsSource(TwoFactorAuthenticationDetailsSource)
// end::authenticationDetailsSource[]

// tag::coordinateValidatorBeanDefinition[]
    coordinateValidator(CoordinateValidatorService)
// end::coordinateValidatorBeanDefinition[]

// tag::twoFactorAuthenticationProviderBeanDefinition[]
    twoFactorAuthenticationProvider(TwoFactorAuthenticationProvider) {
        coordinateValidator = ref('coordinateValidator')
        userDetailsService = ref('userDetailsService')
        passwordEncoder = ref('passwordEncoder')
        userCache = ref('userCache')
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        authoritiesMapper = ref('authoritiesMapper')
        hideUserNotFoundExceptions = true
    }
// end::twoFactorAuthenticationProviderBeanDefinition[]
}

