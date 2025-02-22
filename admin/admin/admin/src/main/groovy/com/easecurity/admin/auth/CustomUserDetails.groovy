package com.easecurity.admin.auth


import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class CustomUserDetails extends GrailsUser {

    final String fullName
    final String avatar
    final String email
    final Date lastLoginTime
    final Integer pdErrorTimes

    CustomUserDetails(String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<GrantedAuthority> authorities,
                      String id, String fullName, String avatar, String email, Date lastLoginTime, Integer pdErrorTimes) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities, id)

        this.fullName = fullName
        this.email = email
        this.avatar = avatar ? avatar : '/adminUI/images/user.gif'
        this.lastLoginTime = lastLoginTime
        this.pdErrorTimes = pdErrorTimes
    }
}
