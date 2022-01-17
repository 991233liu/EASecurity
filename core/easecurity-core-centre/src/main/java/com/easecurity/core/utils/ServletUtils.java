package com.easecurity.core.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import com.easecurity.core.authentication.form.CustomUserDetails;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class ServletUtils {
    /**
     * 获得当前登录的用户Bean
     */
    public static UserDetails getCurrentUser() {
//	return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if (principal instanceof UserDetails) { // 数据库登录用户
	    return (UserDetails) principal;
	} else if (principal == null || principal instanceof String) { // 匿名登录
	    UserBuilder userBuilder = CustomUserDetails.withUsername("anonymousUser");
	    userBuilder.password("anonymousUser");
	    userBuilder.authorities("ROLE_ANONYMOUS");
	    return userBuilder.build();
	} else { // 其它待开发类型
	    // TODO 其它待开发类型
	    return null;
	}
//        HttpServletRequest request = ServletUtils.getRequest()
//        if (request == null) return null
//        UserDetails principal = BeanUtils.getBean('springSecurityService').getPrincipal()
//        User user = (User) request.getAttribute("user")
//        if ( user != null ) return user
//
//        UserDetails principal = (UserDetails) springSecurityService.getPrincipal()
//        if ( null == principal || principal.getUsername() == GrailsAnonymousAuthenticationToken.USERNAME ) {
//            // 匿名用户
//            user = new User(username:"anonymous")
//            user.discard()
//        } else if ( principal instanceof GrailsUser ) {
//            // 数据库登录
//            user = User.get( (Long) ((GrailsUser) principal).getId() )
//        } else if ( principal instanceof LdapUserDetails ) {
//            // ldap登录
//            //user = User.findByUsername( principal.getUsername(), [cache:true])
//            HttpSession session = request.getSession()
//            user = User.findByUsername( session.getAttribute(Constants.SS_USER_NAME), [cache:true])
//        }
//
//        if ( request ) request.setAttribute("user", user)
    }

    /**
     * 获得当前登录用户的用户员工身份
     */
    public static com.easecurity.core.authentication.UserDetails getCurrentUserDetails() {
	UserDetails currentUser = getCurrentUser();
	if (currentUser == null || "anonymousUser".equals(currentUser.getUsername())) { // 未登录时
	    return null;
	} else { // 登录用户
	    com.easecurity.core.authentication.UserDetails userDetails = (com.easecurity.core.authentication.UserDetails) getSession().getAttribute("userDetails");
	    if (userDetails == null) {
		UserService userService = (UserService) BeanUtils.getBean("userService");
		UserDo userDo = userService.getUserDoByAccount(currentUser.getUsername());
		userDetails = new com.easecurity.core.authentication.UserDetails();
		userDetails.id = userDo.user.id;
		userDetails.account = userDo.user.account;
		userDetails.identities = userDo.user.identities;
		if (userDo.userinfo != null) {
		    userDetails.name = userDo.userinfo.name;
		    userDetails.icon = userDo.userinfo.icon;
		}
		getSession().setAttribute("userDetails", userDetails);
	    }
	    return userDetails;
	}
    }

    /**
     * 获得当前请求的request对象
     * <p>
     *
     * @return request，如果没有请求，则返回null，如从后台直接调用时
     */
    public static HttpServletRequest getRequest() {
	ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	return ra.getRequest();
    }

    /**
     * 获得当前请求的response对象
     * <p>
     *
     * @return 如果没有请求，则返回null，如从后台直接调用时
     */
    public static HttpServletResponse getResponse() {
	ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	return ra.getResponse();
    }

    /**
     * 获得当前请求用户的session
     * <p>
     *
     * @return session，如果没有请求，则返回null，如从后台直接调用时
     */
    public static HttpSession getSession() {
	try {
	    return getRequest().getSession(false);
	} catch (Exception e) {
	    return null;
	}
    }
}