// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.easecurity.admin.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.easecurity.admin.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'com.easecurity.admin.auth.Role'

//grails.plugin.springsecurity.password.algorithm = 'SHA-256'
//grails.plugin.springsecurity.password.hash.iterations = 1
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.password.bcrypt.logrounds = 15

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
        [pattern: '/user/**', access: ['ROLE_ADMIN']],
        [pattern: '/**', access: ['ROLE_USER', 'ROLE_PM', 'ROLE_ADMIN']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**', filters: 'JOINED_FILTERS']
]

