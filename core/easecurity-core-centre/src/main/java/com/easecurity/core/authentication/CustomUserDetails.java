package com.easecurity.core.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private static final long serialVersionUID = -7665150614803466540L;

    public final String id;
    public final String fullName;
    public final String icon;
    public final String identities;
    public final Date lastLoginTime;
    public final Integer pdErrorTimes;
    public final Boolean isAnonymousUser;

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
	    Collection<GrantedAuthority> authorities, String id, String fullName, String icon, String identities, Date lastLoginTime, Integer pdErrorTimes) {
	super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

	this.id = id;
	this.fullName = fullName;
	this.icon = icon;
	this.identities = identities;
	this.lastLoginTime = lastLoginTime;
	this.pdErrorTimes = pdErrorTimes;
	this.isAnonymousUser = false;
//	CustomUserDetails(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
//	    authorities, id, fullName, icon, lastLoginTime, pdErrorTimes, Boolean.FALSE);
    }

    public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
	    Collection<GrantedAuthority> authorities, String id, String fullName, String icon, String identities, Date lastLoginTime, Integer pdErrorTimes, Boolean isAnonymousUser) {
	super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

	this.id = id;
	this.fullName = fullName;
	this.icon = icon;
	this.identities = identities;
	this.lastLoginTime = lastLoginTime;
	this.pdErrorTimes = pdErrorTimes;
	this.isAnonymousUser = isAnonymousUser;
    }

    /**
     * 获取一个匿名登录用户
     */
    public static CustomUserDetails getAnonymousUser() {
//	return new CustomUserDetails("anonymousUser", "", true, true, true, true, (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), "anonymousUser", "", "",
//		new Date(), Integer.valueOf(0), Boolean.TRUE);
	Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>();
	authorities.add((GrantedAuthority) new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
	return new CustomUserDetails("anonymousUser", "", true, true, true, true, authorities, "anonymousUser", null, null, null, null, null, Boolean.TRUE);
    }

    public boolean isAnonymousUser() {
	return isAnonymousUser;
    }
}
