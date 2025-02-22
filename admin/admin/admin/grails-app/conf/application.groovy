// 双因子认证
grails.plugin.springsecurity.providerNames = [
        'twoFactorAuthenticationProvider',
        'anonymousAuthenticationProvider',
        'rememberMeAuthenticationProvider']

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/login/success'
//grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/authfail'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.easecurity.admin.auth.UserAdmin'
grails.plugin.springsecurity.userLookup.usernamePropertyName = 'account'
//grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.easecurity.admin.core.r.RoleUser'
//grails.plugin.springsecurity.authority.className = 'com.easecurity.admin.core.r.Role'
//grails.plugin.springsecurity.authority.nameField = 'code'

//grails.plugin.springsecurity.password.algorithm = 'SHA-256'
//grails.plugin.springsecurity.password.hash.iterations = 1
//grails.plugin.springsecurity.password.algorithm = 'bcrypt'
//grails.plugin.springsecurity.password.bcrypt.logrounds = 15

grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/', access: ['permitAll']],
        [pattern: '/error', access: ['permitAll']],
        [pattern: '/index', access: ['permitAll']],
        [pattern: '/index.gsp', access: ['permitAll']],
        [pattern: '/shutdown', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/**/js/**', access: ['permitAll']],
        [pattern: '/**/css/**', access: ['permitAll']],
        [pattern: '/**/images/**', access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],
        [pattern: '/auth/**', access: ['permitAll']],
//        [pattern: '/**', access: ['ROLE_root#admin']]
//        [pattern: '/**', access: ['IS_AUTHENTICATED_FULLY']]
//        [pattern: '/**', access: 'isAuthenticated()']
        [pattern: '/**', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**', filters: 'JOINED_FILTERS']
]
