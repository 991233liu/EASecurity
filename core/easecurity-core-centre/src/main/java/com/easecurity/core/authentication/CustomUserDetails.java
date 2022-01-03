package com.easecurity.core.authentication;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private static final long serialVersionUID = -7665150614803466540L;

    final String id;
    final String fullName;
    final String icon;
    final Date lastLoginTime;
    final Integer pdErrorTimes;

    CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
	    Collection<GrantedAuthority> authorities, String id, String fullName, String icon, Date lastLoginTime, Integer pdErrorTimes) {
	super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

	this.id = id;
	this.fullName = fullName;
	this.icon = icon;
	this.lastLoginTime = lastLoginTime;
	this.pdErrorTimes = pdErrorTimes;
    }
}
