/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easecurity.core.access.annotation.EaSecuredIP;
import com.easecurity.core.basis.UriDo;
import com.easecurity.core.basis.UriService;

/**
 * 企业安全中心数据交换服务
 *
 */
@Controller
@RequestMapping("/data")
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private UriService uriService;

    /**
     * 最后修改时间，"0"标示不存在
     */
    private static volatile String lastModified = "1";
    /**
     * 控制列表
     */
    private static volatile HashMap<String, Object> allEas = null;

    /**
     * 当前登录人的菜单？？？？
     */
    @RequestMapping("/alleas")
    @EaSecuredIP
    public void getAllEas(HttpServletRequest request, HttpServletResponse response) {
	String lm = request.getParameter("lastModified");
	if (!lastModified.equals(lm)) {
	    // TODO 加载？？？肯定不是这个地方的，哈哈。
	    allEas = new HashMap<>();
	    // TODO 每次从缓存中获取后，hashCode都会发生变化！！！
	    allEas.put("allUriDos", uriService.getAllUriDos());
	    lastModified = String.valueOf(uriService.getAllUriDos().hashCode());
	    allEas.put("lastModified", lastModified);

	    ObjectOutputStream oos = null;
	    try {
		oos = new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(allEas);
		oos.flush();
	    } catch (IOException e) {
		log.error("推送控制列表时，数据流读取异常:", e);
		e.printStackTrace();
	    } finally {
		if (null != oos) {
		    try {
			oos.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	} else {
	    response.setStatus(304);
	}
    }

    /**
     * 保存UriDo配置
     */
    @RequestMapping("/saveurido")
    @EaSecuredIP
    public void saveUriDo(HttpServletRequest request, HttpServletResponse response) {
	ObjectInputStream ois = null;
	try {
	    ois = new ObjectInputStream(request.getInputStream());
	    UriDo lUriDo = (UriDo) ois.readObject();
	    uriService.saveUriDo(lUriDo);
	    response.setStatus(200);
	} catch (IOException e) {
	    log.error("从客户端保存UriDo配置时，数据流读取异常:", e);
	    response.setStatus(500);
	} catch (ClassNotFoundException e) {
	    log.error("从客户端保存UriDo配置时，反序列化异常:", e);
	    response.setStatus(500);
	} finally {
	    if (ois != null)
		try {
		    ois.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
    }
}
