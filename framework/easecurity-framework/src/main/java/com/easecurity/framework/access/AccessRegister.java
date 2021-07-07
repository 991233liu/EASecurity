/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easecurity.core.basis.UriDo;

/**
 * 控制列表加载方法。每分钟从远端拉取一次最新控制列表，如果无变化，则不重载。
 *
 */
public class AccessRegister {
    private static final Logger log = LoggerFactory.getLogger(AccessRegister.class);

    private static AccessRegister instance = null;
    private long threadSleepTime = 60 * 1000l; // 线程沉睡时间
    private int connectTimeout = 30 * 1000; // 链接超时时间

    /**
     * 企业安全中心服务地址
     */
    private String easCentreUrl = null;
    /**
     * 最后修改时间，"0"标示不存在
     */
    private static volatile String lastModified = "0";
    /**
     * 控制列表
     */
    private static volatile HashMap<String, Object> allEas = null;

    private AccessRegister(String easCentreUrl) {
	this.easCentreUrl = easCentreUrl;
	new Thread("AccessRegisterThread") {
	    public void run() {
		while (true) {
		    try {
			Thread.sleep(threadSleepTime);
		    } catch (InterruptedException e) {
			log.error("定时拉取控制列表时出现异常:", e);
		    }
		    getAllEas();
		}
	    }
	}.start();
    }

    public static synchronized final AccessRegister getInstance(String easCentreUrl) {
	if (instance == null && easCentreUrl != null && !easCentreUrl.isEmpty())
	    instance = new AccessRegister(easCentreUrl);
	return instance;
    }

    /**
     * 检查并更新控制列表（从SecurityCentre拉取）
     */
    private void getAllEas() {
	if (easCentreUrl != null) { // 类没有初始化完成时，不能拉！
	    checkAndUpdate();
	    // TODO 做点什么呢？
	}
    }

    /**
     * 检查并更新控制列表
     */
    @SuppressWarnings("unchecked")
    private boolean checkAndUpdate() {
	ObjectInputStream ois = null;
	HashMap<String, Object> map = null;
	try {
	    String uri = easCentreUrl + "/data/alleas?lastModified=" + lastModified;
	    HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
	    connection.setConnectTimeout(connectTimeout);
	    connection.setReadTimeout(connectTimeout);
	    connection.setDoInput(true);
	    connection.setDoOutput(true);
	    connection.setRequestMethod("GET");
	    connection.addRequestProperty("User-Agent", "Mozilla/99.0");
	    connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	    connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
	    connection.addRequestProperty("Accept-Charset", "utf-8,ISO-8859-1,gbk,gb2312;q=0.7,*;q=0.7");
	    connection.connect();
	    int state = connection.getResponseCode();
	    if (state == 200) { // 列表发生变化，读取新的控制列表
		ois = new ObjectInputStream(connection.getInputStream());
		map = (HashMap<String, Object>) ois.readObject();
		lastModified = (String) map.get("lastModified");
		// TODO 未完待续
		allEas = map;
		return true;
	    } else if (state == 304) { // 列表未发生变化
		return true;
	    } else { // 服务器返回错误
		return false;
	    }
	} catch (IOException e) {
	    log.error("拉取控制列表时，数据流读取异常:", e);
	} catch (ClassNotFoundException e) {
	    log.error("拉取控制列表时，反序列化异常:", e);
	    e.printStackTrace();
	} finally {
	    if (ois != null)
		try {
		    ois.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	return false;
    }

    @SuppressWarnings("unchecked")
    public Map<String, UriDo> getAllUriDos() {
	if (allEas == null) { // 如果它不存在，先初始化一下
	    getAllEas();
	}
	return allEas == null ? null : (Map<String, UriDo>) allEas.get("allUriDos");
    }
}
