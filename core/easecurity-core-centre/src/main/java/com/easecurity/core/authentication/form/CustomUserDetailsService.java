package com.easecurity.core.authentication.form;

import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.b.UserEnum;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

//    /**
//     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least one
//     * role, so we give a user with no granted roles this one which gets past that
//     * restriction but doesn't grant anything.
//     */
//    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)];

    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
	return loadUserByUsername(username);
    }

    // TODO 登录性能待优化
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	UserDo user = userService.getUserDoForLogin(username);
	if (user == null)
	    throw new UsernameNotFoundException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));

	// TODO 大角色？？？
	// or if you are using role groups:
	// def roles = user.authorities.collect { it.authorities }.flatten().unique()

	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	user.roleUsers.forEach(it -> {
	    authorities.add(new SimpleGrantedAuthority(it.roleCode));
	});

//        if ( log.isDebugEnabled() ) log.debug("syncUser message:uid=" + uid + ",username=" + username + ",mail=" + mail)
	return new CustomUserDetails(username, user.user.pd, user.user.acStatus == UserEnum.AcStatus.ENABLED, !(user.user.pdStatus == UserEnum.PdStatus.EXPIRED),
		!(user.user.pdStatus == UserEnum.PdStatus.EXPIRED), user.user.pdStatus == UserEnum.PdStatus.ENABLED, authorities, user.user.id, user.userinfo.name,
		user.userinfo.icon, user.user.identities, user.user.lastLoginTtime, user.user.pdErrorTimes);
    }
}
