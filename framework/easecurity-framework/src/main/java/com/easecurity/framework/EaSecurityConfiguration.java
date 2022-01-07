/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

/**
 * EasSecurity配置
 *
 */
public class EaSecurityConfiguration {

    /**
     * 企业安全中心服务地址
     */
    private String easCentreUrl;
    
    /**
     * 与企业安全中心通信间隔（单位毫秒）。默认1分钟。
     */
    private long threadSleepTime = 60 * 1000l;
    
    /**
     * 与企业安全中心通信链接超时时间（单位毫秒）。默认30秒。
     */
    private int connectTimeout = 30 * 1000; // 链接超时时间

    public EaSecurityConfiguration(String easCentreUrl) {
	setEasCentreUrl(easCentreUrl);
    }

    public String getEasCentreUrl() {
	return easCentreUrl;
    }

    public void setEasCentreUrl(String easCentreUrl) {
	if (easCentreUrl == null || easCentreUrl.isEmpty())
	    throw new RuntimeException("---## EaSecurityConfiguration，easCentreUrl不能为空。easCentreUrl=" + easCentreUrl);
	this.easCentreUrl = easCentreUrl;
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
    
//    public void afterPropertiesSet(){
//	// TODO 做点啥呢？
//    }
}
