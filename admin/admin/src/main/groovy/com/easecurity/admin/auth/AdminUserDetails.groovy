package com.easecurity.admin.auth

import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

@CompileStatic
class AdminUserDetails extends User {

    // extra instance variables
    final String uid
    final String email

    AdminUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                  boolean credentialsNonExpired, boolean accountNonLocked,
                  Collection<GrantedAuthority> authorities, String uid, String email) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities)
        System.out.println(authorities);
        System.out.println(username);
        this.uid = uid
        this.email = email
    }
}
