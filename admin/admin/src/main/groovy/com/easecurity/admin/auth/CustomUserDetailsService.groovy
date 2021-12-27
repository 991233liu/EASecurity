package com.easecurity.admin.auth

import com.easecurity.core.basis.b.UserEnum
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
        UserAdmin user = UserAdmin.findByAccount(username)
        if (!user) throw new NoStackUsernameNotFoundException()

        def roles = user.authorities

        // TODO 大角色？？？
        // or if you are using role groups:
        // def roles = user.authorities.collect { it.authorities }.flatten().unique()

        def authorities = roles.collect {
            new SimpleGrantedAuthority(it)
        }

//        if ( log.isDebugEnabled() ) log.debug("syncUser message:uid=" + uid + ",username=" + username + ",mail=" + mail)
        return new CustomUserDetails(user.account, user.password, user.user.acStatus == UserEnum.AcStatus.ENABLED,
                !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED), !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED), user.user.pdStatus == UserEnum.PdStatus.ENABLED,
                authorities ?: NO_ROLES, user.id,
                "fullname")
    }
}
