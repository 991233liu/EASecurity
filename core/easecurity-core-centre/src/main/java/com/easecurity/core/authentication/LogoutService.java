/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 注销相关服务
 *
 */
@Service
public class LogoutService {
    private static final Logger log = LoggerFactory.getLogger(LogoutService.class);

    public void logoutByCookie(String url, Cookie[] cookies) {
	try {
	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    setDefaultConfig(connection);
	    if (cookies != null && cookies.length > 0) {
		String cookiestr = "";
		for (Cookie cookie : cookies) {
		    cookiestr += ";" + cookie.getName() + "=" + cookie.getValue();
		}
		connection.addRequestProperty("Cookie", cookiestr.substring(1));
	    }
	    connection.connect();
	    int state = connection.getResponseCode();
	    if (state == 200) { // 服务器处理正常
		log.debug("远程系统退出登录成功：{}", url);
	    } else { // 服务器返回错误
		log.info("远程系统退出登录失败：state={} url={}", state, url);
	    }
	} catch (IOException e) {
	    log.error("远程系统退出登录异常:" + url, e);
	}
    }

    /**
     * 设置HttpURLConnection的链接属性
     * 
     */
    private void setDefaultConfig(HttpURLConnection connection) throws ProtocolException {
	connection.setConnectTimeout(3000);
	connection.setReadTimeout(1000);
	connection.setDoInput(true);
	connection.setDoOutput(false);
	connection.setUseCaches(false);
	connection.setRequestMethod("GET");
	connection.addRequestProperty("User-Agent", "Mozilla/99.0");
	connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
	connection.addRequestProperty("Accept-Charset", "utf-8,ISO-8859-1,gbk,gb2312;q=0.7,*;q=0.7");
    }
}
