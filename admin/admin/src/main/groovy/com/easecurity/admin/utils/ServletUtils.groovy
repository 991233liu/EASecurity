package com.easecurity.admin.utils

import groovy.transform.CompileStatic
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletWebRequest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class ServletUtils {
    /**
     * 获得当前登录的用户Bean
     */
    static def getCurrentUser() {
        HttpServletRequest request = ServletUtils.getRequest()
        if ( request == null ) return null
        UserDetails principal = BeanUtils.getBean('springSecurityService').getPrincipal()
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
     * 获得当前登录的用户员工身份
     */
    static def getCurrentStaff() {
        def staff = getSession().getAttribute("CurrentStaff")
//        return staff ? staff : Staff.findByUid(ServletUtils.currentUser?.username, [cache:true])
//        HttpServletRequest request = ServletUtils.getRequest()
//        if ( request == null ) return null
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
     * 获得当前请求的request对象<p>
     *
     * @return request，如果没有请求，则返回null，如从后台直接调用时
     */
    @CompileStatic
    static HttpServletRequest getRequest() {
        ServletWebRequest ra = (ServletWebRequest) RequestContextHolder.getRequestAttributes()
        return ra?.getRequest()
    }

    /**
     * 获得当前请求的response对象<p>
     *
     * @return 如果没有请求，则返回null，如从后台直接调用时
     */
    @CompileStatic
    static HttpServletResponse getResponse() {
        ServletWebRequest ra = (ServletWebRequest) RequestContextHolder.getRequestAttributes()
        return ra?.getResponse()
    }

    /**
     * 获得当前请求用户的session<p>
     *
     * @return session，如果没有请求，则返回null，如从后台直接调用时
     */
    @CompileStatic
    static HttpSession getSession() {
        try {
            return getRequest()?.getSession(false)
        } catch (Exception e) {
            return null
        }
    }
}
