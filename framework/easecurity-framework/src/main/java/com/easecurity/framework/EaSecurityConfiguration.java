/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * EasSecurity配置
 *
 */
public class EaSecurityConfiguration {

    public ServerProperties server;
    
    public JWTProperties jwt;

    /**
     * 安全校验模式：dev(development)、prod(production)。在dev模式下，安全校验标签（功能）会正常执行，
     * 校验结果会输出到日志中（info级别），但是，无论校验结果如何，被保护（控制）的方法仍会被执行。 默认为prod模式。
     */
    public String verification = "prod";
    
    /**
     * 未登录时的跳转页面
     */
    public String noLoginUrl;
    
    /**
     * 未登录时的返回信息，此时http.Status为403
     */
    public String noLoginMessage;
    
    /**
     * gateway中发现访问没有权限的接口时，返回的提示信息
     */
    public String noPermissionMessage;

    /**
     * 设置HttpURLConnection的链接属性
     * 
     */
    public HttpURLConnection setDefaultConfig(HttpURLConnection connection) throws ProtocolException {
	connection.setConnectTimeout(server.getConnectTimeout());
	connection.setReadTimeout(server.getConnectTimeout());
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setUseCaches(false);
	connection.setRequestMethod("POST");
	connection.addRequestProperty("User-Agent", "Mozilla/99.0");
	connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
	connection.addRequestProperty("Accept-Charset", "utf-8,ISO-8859-1,gbk,gb2312;q=0.7,*;q=0.7");
	return connection;
    }

    /**
     * 安全管控是否处于开发模式
     */
    public boolean isDevelopmentMode() {
	if (verification != null && "dev".equals(verification.toLowerCase()))
	    return true;
	else
	    return false;
    }

    /**
     * 安全管控是否处于生产模式
     */
    public boolean isProductionMode() {
	return !isDevelopmentMode();
    }

    public ServerProperties getServer() {
	return server;
    }

    public void setServer(ServerProperties server) {
	this.server = server;
    }

    public String getVerification() {
	return verification;
    }

    public void setVerification(String verification) {
	this.verification = verification;
    }

    public JWTProperties getJwt() {
	return jwt;
    }

    public void setJwt(JWTProperties jwt) {
	this.jwt = jwt;
    }

    public String getNoLoginUrl() {
        return noLoginUrl;
    }

    public void setNoLoginUrl(String noLoginUrl) {
        this.noLoginUrl = noLoginUrl;
    }

    public String getNoLoginMessage() {
        return noLoginMessage;
    }

    public void setNoLoginMessage(String noLoginMessage) {
        this.noLoginMessage = noLoginMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
    }

    public static class ServerProperties {
	/**
	 * 企业安全中心服务地址
	 */
	private String url;

	/**
	 * 与企业安全中心通信间隔（单位毫秒）。默认1分钟。
	 */
	private long threadSleepTime = 60 * 1000l;

	/**
	 * 与企业安全中心通信链接超时时间（单位毫秒）。默认30秒。
	 */
	private int connectTimeout = 30 * 1000; // 链接超时时间

	public String getUrl() {
	    return url;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	public long getThreadSleepTime() {
	    return threadSleepTime;
	}

	public void setThreadSleepTime(long threadSleepTime) {
	    this.threadSleepTime = threadSleepTime;
	}

	public int getConnectTimeout() {
	    return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
	    this.connectTimeout = connectTimeout;
	}
    }

    public static class JWTProperties {

	/**
	 * RSA公钥
	 */
	private String publicKey;

	public String getPublicKey() {
	    return publicKey;
	}

	public void setPublicKey(String publicKey) {
	    this.publicKey = publicKey;
	}
    }

}
