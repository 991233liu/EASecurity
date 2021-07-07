/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 企业安全中心数据交换服务
 *
 */
@Controller
@RequestMapping("/data")
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    /**
     * 最后修改时间，"0"标示不存在
     */
    private static volatile String lastModified = "1";
    /**
     * 控制列表
     */
    private static volatile HashMap<String, Object> allEas = null;

    /**
     * 当前登录人的菜单
     */
    @RequestMapping("/alleas")
    public void getAllEas(HttpServletRequest request, HttpServletResponse response) {
	String lm = request.getParameter("lastModified");
	if (!lastModified.equals(lm)) {
	    allEas = new HashMap<>();
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
}
