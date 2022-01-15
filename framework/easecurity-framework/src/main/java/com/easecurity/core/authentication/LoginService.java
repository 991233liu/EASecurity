/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.easecurity.framework.EaSecurityConfiguration;

/**
 * 登录相关服务
 *
 */
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private EaSecurityConfiguration eaSecurityConfiguration;

    public LoginService(EaSecurityConfiguration eaSecurityConfiguration) {
	this.eaSecurityConfiguration = eaSecurityConfiguration;
    }

    /**
     * 获取当前登录人
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public UserDetails getCurrentUser(HttpServletRequest request) throws IOException {
	Cookie[] cookies = request.getCookies();
	for (Cookie cookie : cookies) {
	    if (cookie.getName().equals("EASECURITY_S")) {
		return _getCurrentUser(cookie);
	    }
	}
	return _getCurrentUser(null);
    }

    private UserDetails _getCurrentUser(Cookie cookie) throws IOException {
	if (cookie == null) { // TODO 未登录用户 或者 其它待开发的认证方式
	    return null;
	} else { // 存在cookie，从远端获取认证用户
	    BufferedReader br = null;
	    try {
		String uri = eaSecurityConfiguration.getEasCentreUrl() + "/auth/currentUser";
		HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
		connection = eaSecurityConfiguration.setDefaultConfig(connection);
		connection.setRequestMethod("GET");
		connection.addRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
		connection.connect();
		int state = connection.getResponseCode();
		if (state == 200) { // 已登录用户
		    StringBuilder sb = new StringBuilder();
		    String line;
		    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    while ((line = br.readLine()) != null) {
			sb.append(line);
		    }
		    String str = sb.toString().trim();
		    return (UserDetails) JSON.parseObject(str, UserDetails.class);
		} else if (state == 203) { // 匿名访问
		    return null;
		} else { // 服务器返回错误
		    log.error("获取当前登录人时，服务器报错，getResponseCode:{}", state);
		    return null;
		}
	    } catch (IOException e) {
		log.error("获取当前登录人时，数据流读取异常:", e);
		throw e;
	    } finally {
		if (br != null)
		    try {
			br.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
	    }
	}
    }
}
