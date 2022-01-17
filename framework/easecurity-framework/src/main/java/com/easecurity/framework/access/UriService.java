/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.core.basis.UriDo;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.re.Uri;
import com.easecurity.util.JsonUtils;

/**
 * 接口（访问）访问控制服务
 *
 */
public class UriService {
    private static final Logger log = LoggerFactory.getLogger(UriService.class);

    AccessRegister accessRegister = null;

    private static volatile Map<String, UriDo> uriDos = new HashMap<>();
    private static volatile Map<String, UriDo> localeUriDos = new HashMap<>();
    private static volatile long lastModifyTime = -1;

    public UriService(AccessRegister accessRegister) {
	this.accessRegister = accessRegister;
    }

    /**
     * 更新URI的授权信息。如果URI已经存在，则更新状态以外其它信息；如果不存在则新建
     * 
     * @param eas
     * @param uri
     * @param classFullName
     * @param methodName
     * @param methodSignature
     */
    public void saveUriPermissions(EaSecured eas, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo lUriDo = null;
	UriDo uriDo = null;
	try {
	    boolean lUriDoFirst = false;
	    if (!localeUriDos.containsKey(uri)) { // 启动后第一次被访问
		synchronized (this) {
		    if (!localeUriDos.containsKey(uri)) {
			lUriDo = createLocaleUriDo(eas, uri, classFullName, methodName, methodSignature);
			localeUriDos.put(uri, lUriDo);
			lUriDoFirst = true;
		    } else {
			lUriDo = localeUriDos.get(uri);
		    }
		}
	    }
	    uriDos = accessRegister.getAllUriDos();
	    if (uriDos.containsKey(uri)) {
		lUriDo = localeUriDos.get(uri);
		if (System.currentTimeMillis() > lastModifyTime) { // 每分钟更新一次
		    uriDo = uriDos.get(uri);
		    synchronized (this) {
			updateLocaleUriDoIdAndStatus(lUriDo, uriDo);
			lastModifyTime = System.currentTimeMillis() + 60000;
		    }
		    if (lUriDoFirst) {
			// 第一次启动时更新一次
			accessRegister.saveUriEas(lUriDo);
		    }
		}
	    } else { // 不存在时，则新建配置
		// 每次启动更新一次
		if (lUriDoFirst)
		    accessRegister.saveUriEas(lUriDo);
	    }
	} catch (Exception e) {
	    log.error("更新URI的授权信息时出现异常：" + lUriDo + "==" + uriDo, e);
	}
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured eas, String uri, UserDetails userDo) {
	EasType type = eas.type();
	Map<String, String> allIdentities = new HashMap<>();
	if (userDo != null)
	    allIdentities = userDo.allIdentitiesWithMap();
	UriDo uriDo = null;
	if ("allUser".equals(eas.value())) { // 所有登录用户可访问
	    if (!allIdentities.isEmpty())
		return true;
	    else
		return false;
	} else if (type == EasType.SOURCE_ONLY) { // 源码模式
	    uriDo = localeUriDos.get(uri);
	} else if (type == EasType.DATABASE_ONLY) { // 数据库模式
	    uriDo = uriDos.get(uri);
	} else { // 数据库+源码模式
	    if (localeUriDos.containsKey(uri))
		uriDo = localeUriDos.get(uri);
	    else
		uriDo = uriDos.get(uri);
	}
	return havePermission(uriDo, allIdentities, uri);
    }

    // TODO 遍历权限，判断每种情况
    private boolean havePermission(UriDo uriDo, Map<String, String> allIdentities, String uri) {
	boolean[] flag = { false };
	if (uriDo != null) {
	    allIdentities.forEach((k, v) -> {
		switch (k) {
		case "user":
		    break;
		case "org":
		    for (String id : v.split(",")) {
			if (uriDo.havePermissionByOrgId(id)) {
			    flag[0] = true;
			    return;
			}
		    }
		    break;
		}
	    });
	} else {
	    log.error("---## 发现了一个不存在数据库的URI，请联系管理员，URI为：{}", uri);
	}
	return flag[0];
    }

    /**
     * 新建本地配置
     */
    // TODO 遍历属性
    @SuppressWarnings("unchecked")
    private UriDo createLocaleUriDo(EaSecured eas, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo uriDo = new UriDo();
	Uri u = new Uri();
	u.id = -1; // 还没有存储在数据库中的数据，ID全部为-1
	u.uri = uri;
	u.classFullName = classFullName;
	u.methodName = methodName;
	u.methodSignature = methodSignature;
	u.easType = eas.type();
	u.fromTo = "2";
	u.status = "0";
	uriDo.uri = u;
	String org = eas.org();
	if (!org.isEmpty()) {
	    uriDo.uriOrg = new ArrayList<>();
	    Map<String, Object> allOrgs = (Map<String, Object>) JsonUtils.jsonToObject(org);
	    allOrgs.forEach((k, v) -> {
		// TODO 遍历，其它方式；其它方式下需要预先转化为id？？？
		switch (k) {
		case "id":
		    if (v instanceof String) {
			UriOrg uo = new UriOrg();
			uo.uriid = u.id;
			uo.orgid = Integer.parseInt((String) v);
			uo.status = "0";
			uo.fromTo = "2";
			uriDo.uriOrg.add(uo);
		    } else {
			((List<String>) v).forEach(item -> {
			    UriOrg uo = new UriOrg();
			    uo.uriid = u.id;
			    uo.orgid = Integer.parseInt((String) item);
			    uo.status = "0";
			    uo.fromTo = "2";
			    uriDo.uriOrg.add(uo);
			});
		    }
		    break;

		default:
		    break;
		}
	    });
	}
	return uriDo;
    }

    /**
     * 从数据库汇总更新本地状态和ID
     */
    // TODO 遍历所有属性
    private void updateLocaleUriDoIdAndStatus(UriDo lUriDo, UriDo uriDo) {
	lUriDo.uri.id = uriDo.uri.id;
	lUriDo.uri.status = uriDo.uri.status;
	if (lUriDo.uriOrg != null)
	    lUriDo.uriOrg.forEach(item -> {
		for (UriOrg uo : uriDo.uriOrg) {
		    if (item.orgid == uo.orgid) {
			item.id = uo.id;
			item.uriid = uo.uriid;
			item.status = uo.status;
			break;
		    }
		}
	    });
    }
}
