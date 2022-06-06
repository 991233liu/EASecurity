/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easecurity.core.basis.UriDo;
import com.easecurity.framework.EaSecurityConfiguration;

/**
 * 控制列表加载方法。每分钟从远端拉取一次最新控制列表，如果无变化，则不重载。
 *
 */
public class AccessRegister {
    private static final Logger log = LoggerFactory.getLogger(AccessRegister.class);

    private static AccessRegister instance = null;

    private EaSecurityConfiguration eaSecurityConfiguration = null;
    private static ArrayBlockingQueue<UriDo> taskQueue = new ArrayBlockingQueue<UriDo>(10240, true);
    /**
     * 最后修改时间，"0"表示不存在
     */
    private static volatile String lastModified = "0";
    /**
     * 控制列表
     */
    private static volatile HashMap<String, Object> allEas = null;

    private AccessRegister(EaSecurityConfiguration securityConfiguration) {
	this.eaSecurityConfiguration = securityConfiguration;
	new Thread("AccessRegisterThread") {
	    public void run() {
		while (true) {
		    try {
			Thread.sleep(eaSecurityConfiguration.server.getThreadSleepTime());
		    } catch (InterruptedException e) {
			log.error("定时拉取控制列表时出现异常:", e);
		    }
		    getAllEas();
		    processQueue();
		}
	    }
	}.start();
    }

    public static synchronized final AccessRegister getInstance(EaSecurityConfiguration eaSecurityConfiguration) {
	if (instance == null)
	    instance = new AccessRegister(eaSecurityConfiguration);
	return instance;
    }

    /**
     * 检查并更新控制列表（从SecurityCentre拉取）
     */
    private synchronized void getAllEas() {
	if (eaSecurityConfiguration != null) { // 类没有初始化完成时，不能拉！
	    // TODO 返回false后，做点什么呢？
	    checkAndUpdate();
	    // 不能为null，防止后续的方法报错
	    allEas = allEas == null ? new HashMap<>() : allEas;
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
	    String uri = eaSecurityConfiguration.server.getUrl() + "/data/alleas?lastModified=" + lastModified;
	    HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
	    connection = eaSecurityConfiguration.setDefaultConfig(connection);
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

    /**
     * 消息队列处理，发送到远端服务器
     */
    private void processQueue() {
	log.debug(">>>>>>>>>>current taskQueue has " + taskQueue.size() + " tasks");
	UriDo lUriDo = null;
	List<UriDo> list = new ArrayList<UriDo>();
	try {
	    while (!taskQueue.isEmpty()) { // 如果消息队列中存在消息，则循环处理，直到所有消息处理完毕。
		lUriDo = taskQueue.poll(20, TimeUnit.MILLISECONDS);
		// 即便前面显示有待处理的消息，仍有可能获取不到。因为已经被其它并发的线程拿走了。
		if (lUriDo == null)
		    continue;
		if (!sendUriDoToServer(lUriDo)) { // 发送失败后，重新放入待发送队列
		    list.add(lUriDo);
		}
	    }
	    // 发送失败后，重新放入待发送队列
	    taskQueue.addAll(list);
	} catch (InterruptedException e) {
	    log.error("Process sendUriDoToServer failed: lUriDo={}", lUriDo, e);
	}

    }

    private boolean sendUriDoToServer(UriDo lUriDo) {
	ObjectOutputStream oos = null;
	try {
	    String uri = eaSecurityConfiguration.server.getUrl() + "/data/saveurido?time=" + System.currentTimeMillis();
	    HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
	    connection = eaSecurityConfiguration.setDefaultConfig(connection);
	    connection.connect();
	    oos = new ObjectOutputStream(connection.getOutputStream());
	    oos.writeObject(lUriDo);
	    oos.flush();
	    int state = connection.getResponseCode();
	    if (state == 200) { // 服务器处理正常
		return true;
	    } else { // 服务器返回错误
		return false;
	    }
	} catch (IOException e) {
	    log.error("发送UriDo到远端服务器时，数据流推送异常:" + lUriDo.uri.uri, e);
	} finally {
	    if (null != oos) {
		try {
		    oos.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return false;
    }

    /**
     * 获取所有的UriDo
     */
    @SuppressWarnings("unchecked")
    public Map<String, UriDo> getAllUriDos() {
	if (allEas == null) { // 如果它不存在，先初始化一下
	    getAllEas();
	}
	return (Map<String, UriDo>) allEas.get("allUriDos") == null ? new HashMap<>() : (Map<String, UriDo>) allEas.get("allUriDos");
    }

    /**
     * 获取当前的EasSecurity配置
     */
    public EaSecurityConfiguration getEaSecurityConfiguration() {
	return eaSecurityConfiguration;
    }

    /**
     * 保存本地UriDo配置
     */
    public void saveUriEas(UriDo lUriDo) {
	try {
	    taskQueue.add(lUriDo); // 不等待情况
	} catch (Exception ex) {
	    log.error("保存本地UriDo配置到待发送队列异常", ex);
	}
    }
}
