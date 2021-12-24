package com.easecurity.admin.auth

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.gorm.transactions.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUserDetailsService implements GrailsUserDetailsService {
    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least
     * one role, so we give a user with no granted roles this one which gets
     * past that restriction but doesn't grant anything.
     */
    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    @Transactional(readOnly = true, noRollbackFor = [IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("--------##1 " + username);
        UserAdmin user = UserAdmin.findByUser(username)
        System.out.println("--------##1 " + user?.password);
        if (!user) throw new NoStackUsernameNotFoundException()

        def roles = user.authorities
        System.out.println("--------##1 " + roles);
        // or if you are using role groups:
        // def roles = user.authorities.collect { it.authorities }.flatten().unique()

        def authorities = roles.collect {
            new SimpleGrantedAuthority(it)
        }
//        def authorities = ['ROLE_USER', 'rootadmin'].collect {
//            new SimpleGrantedAuthority(it)
//        }


        // TODO 密码动态处理，目前domain中写死的
        return new CustomUserDetails(user.user, user.password, true,
                true, true, true,
                authorities ?: NO_ROLES, user.id,
                "fullname")
    }
}
