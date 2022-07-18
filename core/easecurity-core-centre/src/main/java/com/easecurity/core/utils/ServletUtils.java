package com.easecurity.core.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.s.UserToken;
import com.easecurity.util.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class ServletUtils {

    private static LoginService loginService = (LoginService) BeanUtils.getBean(LoginService.class);
    private static UserService userService = (UserService) BeanUtils.getBean(UserService.class);

    /**
     * 获得当前登录用户的用户员工身份
     */
    public static UserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String at = getAccessToken();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) { // 基于session的数据库登录用户
            UserDetails userDetails = (UserDetails) CacheUtil.getSessionCache("userDetails");
            if (userDetails == null) {
                org.springframework.security.core.userdetails.UserDetails currentUser = (org.springframework.security.core.userdetails.UserDetails) principal;
                UserDo userDo = userService.getUserDoByAccount(currentUser.getUsername());
                userDetails = new UserDetails();
                userDetails.id = userDo.user.id;
                userDetails.account = userDo.user.account;
                userDetails.identities = userDo.user.identities;
                if (userDo.userinfo != null) {
                    userDetails.name = userDo.userinfo.name;
                    userDetails.icon = userDo.userinfo.icon;
                }
                CacheUtil.setSessionCache("userDetails", userDetails);
            }
            return userDetails;
        } else if (at != null) { // 基于AccessToken的数据库登录用户
            UserDetails userDetails = (UserDetails) CacheUtil.getAccessTokenCache("userDetails");
            if (userDetails == null) {
                UserToken userToken = loginService.getValidUserToken(at);
                userDetails = JsonUtils.jsonToObject(userToken.userDetails, UserDetails.class);
                CacheUtil.setAccessTokenCache("userDetails", userDetails);
            }
            return userDetails;
        } else if (principal == null || principal instanceof String) { // 匿名登录
//	    UserBuilder userBuilder = CustomUserDetails.withUsername("anonymousUser");
//	    userBuilder.password("anonymousUser");
//	    userBuilder.authorities("ROLE_ANONYMOUS");
            return null;
        } else { // 其它待开发类型
            // TODO 其它待开发类型
            return null;
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

    /**
     * 获得客户端IP地址
     * <p>
     * 
     * @param request
     * @return ip地址
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        if (null == request)
            return null;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        if (ip != null && ip.indexOf(",") > -1)
            ip = ip.substring(0, ip.indexOf(","));
        return ip;
    }

    /**
     * 获取AccessToken（如果存在的话）
     */
    public static String getAccessToken() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String accessToken = request.getHeader("authorization");
            if (accessToken != null && accessToken.indexOf("Bearer") > -1) {
                return accessToken.substring(accessToken.indexOf("Bearer") + 6).trim();
            } else if (request.getHeader("access_token") != null && !request.getHeader("access_token").trim().isEmpty()) {
                return request.getHeader("access_token").trim();
            } else if (request.getParameter("access_token") != null && !request.getParameter("access_token").trim().isEmpty()) {
                return request.getParameter("access_token").trim();
            }
        }
        return null;
    }
}
