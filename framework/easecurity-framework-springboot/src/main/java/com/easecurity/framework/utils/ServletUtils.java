package com.easecurity.framework.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class ServletUtils {

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
}
