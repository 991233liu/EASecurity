package com.easecurity.framework.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class ServletUtils {

//    /**
//     * 获得当前请求的request对象
//     * <p>
//     *
//     * @return request，如果没有请求，则返回null，如从后台直接调用时
//     */
//    public static HttpServletRequest getRequest() {
//	ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//	return ra.getRequest();
//    }
//
//    /**
//     * 获得当前请求的response对象
//     * <p>
//     *
//     * @return 如果没有请求，则返回null，如从后台直接调用时
//     */
//    public static HttpServletResponse getResponse() {
//	ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//	return ra.getResponse();
//    }
//
//    /**
//     * 获得当前请求用户的session
//     * <p>
//     *
//     * @return session，如果没有请求，则返回null，如从后台直接调用时
//     */
//    public static HttpSession getSession() {
//	try {
//	    return getRequest().getSession(false);
//	} catch (Exception e) {
//	    return null;
//	}
//    }

    /**
     * 获得客户端IP地址
     * <p>
     * 
     * @param request
     * @return ip地址
     */
    public static String getClientIpAddr(ServerHttpRequest request) {
        if (null == request)
            return null;
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getHeaders().getFirst("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown ".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))
            ip = request.getRemoteAddress().getHostString();
        if (ip != null && ip.indexOf(",") > -1)
            ip = ip.substring(0, ip.indexOf(","));
        return ip;
    }
}
