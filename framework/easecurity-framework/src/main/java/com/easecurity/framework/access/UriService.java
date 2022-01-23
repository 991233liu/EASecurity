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
import com.easecurity.core.basis.au.UriIp;
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
    private static volatile Map<String, Long> lastModifyTime = new HashMap<>();

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
    public void saveUriPermissions(EaSecured[] eases, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo lUriDo = null;
	UriDo uriDo = null;
	try {
	    if (!localeUriDos.containsKey(uri)) { // 启动后第一次被访问
		synchronized (this) {
		    if (!localeUriDos.containsKey(uri)) {
			lUriDo = createLocaleUriDo(eases, uri, classFullName, methodName, methodSignature);
			localeUriDos.put(uri, lUriDo);
			lastModifyTime.put(uri, (long) -1);
		    }
		}
	    }
	    lUriDo = localeUriDos.get(uri);

	    uriDos = accessRegister.getAllUriDos();
	    if (uriDos.containsKey(uri)) {
		// 数据库中已经存在此配置，则从数据库更新到本地
		if (System.currentTimeMillis() > lastModifyTime.get(uri)) { // 每分钟更新一次
		    uriDo = uriDos.get(uri);
		    synchronized (this) {
			updateLocaleUriDoIdAndStatus(lUriDo, uriDo);
			lastModifyTime.put(uri, System.currentTimeMillis() + 60000);
		    }
		}
		// TODO 开发模式下强制更新数据库？？？新增一个配置吧
	    } else { // 不存在时，则新建配置
		accessRegister.saveUriEas(lUriDo);

		// TODO
		// 有个bug，对于全新的配置，数据库中没有，saveUriEas方法为异步的，那么在很长一段时间内在进行validation时会报NullPointerException
	    }
	} catch (Exception e) {
	    log.error("更新URI的授权信息时出现异常：" + lUriDo + "==" + uriDo, e);
	}
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured[] eases, String uri, UserDetails userDt, String clientIp) {
	boolean validation = true;
	// 多组为and关系，有一个false则false
	for (int i = 0; i < eases.length; i++) {
	    if (!validation(eases[i], i + 1, uri, userDt, clientIp)) {
		validation = false;
		break;
	    }
	}
	return validation;
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured eas, int group, String uri, UserDetails userDt, String clientIp) {
	uriDos = accessRegister.getAllUriDos();

	if ("allUser".equals(eas.value())) { // 所有登录用户可访问
	    if (userDt == null)
		return false;
	    else
		return true;
	}

	UriDo uriDo = localeUriDos.get(uri);
	EasType type = uriDo.uri.easType;
	if (type == EasType.DATABASE_ONLY) { // 数据库模式
	    if (uriDos.containsKey(uri))
		uriDo = uriDos.get(uri);
	    else
		throw new NullPointerException("数据库配置不存在，请检查SecurityCentre是否启动，且网络已经通畅");
	}

	if (uriDo == null) {
	    log.error("---## 发现了一个不存在的UriDo，请联系管理员，URI为：{}", uri);
	    return false;
	}
	Map<String, String> allIdentities = new HashMap<>();
	if (userDt != null)
	    allIdentities = userDt.allIdentitiesWithMap();
	return havePermission(uriDo, group, allIdentities, uri, clientIp);
    }

    /**
     * 校验访问权限（纯数据库模式）
     */
    public boolean validation(String uri, UserDetails userDt, String clientIp) {
	uriDos = accessRegister.getAllUriDos();
	Map<String, String> allIdentities = new HashMap<>();
	if (userDt != null)
	    allIdentities = userDt.allIdentitiesWithMap();
	UriDo uriDo = uriDos.get(uri);
	if (uriDo == null) {
	    log.error("---## 发现了一个不存在数据库的URI，请联系管理员，URI为：{}", uri);
	    return false;
	}
	boolean validation = true;
	// 多组为and关系，有一个false则false
	for (int i = 0; i < uriDo.getMaxGroup(); i++) {
	    if (!havePermission(uriDo, 0, allIdentities, uri, clientIp)) {
		validation = false;
		break;
	    }
	}
	return validation;
    }

    // TODO 遍历权限，判断每种情况
    private boolean havePermission(UriDo uriDo, int group, Map<String, String> allIdentities, String uri, String clientIp) {
	if (uriDo.havePermissionByIp(clientIp, group)) {
	    return true;
	} else {
	    boolean[] flag = { false };
	    allIdentities.forEach((k, v) -> {
		switch (k) {
		case "user":
		    break;
		case "org":
		    for (String id : v.split(",")) {
			if (uriDo.havePermissionByOrgId(id, group)) {
			    flag[0] = true;
			    return;
			}
		    }
		    break;
		}
	    });
	    return flag[0];
	}
    }

    /**
     * 新建本地配置
     */
    // TODO 遍历属性
    private UriDo createLocaleUriDo(EaSecured[] eases, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo uriDo = new UriDo();
	Uri u = new Uri();
	u.id = -1; // 还没有存储在数据库中的数据，ID全部为-1
	u.uri = uri;
	u.classFullName = classFullName;
	u.methodName = methodName;
	u.methodSignature = methodSignature;
	u.easType = getEasType(eases);
	u.fromTo = "2";
	u.status = "0";
	uriDo.uri = u;
	for (int i = 0; i < eases.length; i++) {
	    EaSecured eas = eases[i];
	    createUriOrg(uriDo, eas.org(), i + 1);
	    createUriIp(uriDo, eas.IP(), i + 1);
	}
	return uriDo;
    }

    private void createUriIp(UriDo uriDo, String[] ips, int group) {
	if (ips != null && ips.length > 0) {
	    UriIp uip = new UriIp();
	    uip.uriid = uriDo.uri.id;
	    uip.status = "0";
	    uip.fromTo = "2";
	    uip.group = group;
	    uip.ips = "";
	    for (String string : ips) {
		uip.ips += string + ",";
	    }
	    uip.ips = uip.ips.substring(0, uip.ips.length() - 1);
	    uriDo.uriIp = uip;
	}
    }

    // TODO 遍历属性
    @SuppressWarnings("unchecked")
    private void createUriOrg(UriDo uriDo, String org, int group) {
	if (!org.isEmpty()) {
	    uriDo.uriOrg = new ArrayList<>();
	    Map<String, Object> allOrgs = (Map<String, Object>) JsonUtils.jsonToObject(org);
	    for (String k : allOrgs.keySet()) {
		Object v = allOrgs.get(k);
		// TODO 遍历，其它方式；其它方式下需要预先转化为id？？？
		switch (k) {
		case "id":
		    if (v instanceof String) {
			UriOrg uo = new UriOrg();
			uo.uriid = uriDo.uri.id;
			uo.orgid = Integer.parseInt((String) v);
			uo.group = group;
			uo.status = "0";
			uo.fromTo = "2";
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = new UriOrg();
			    uo.uriid = uriDo.uri.id;
			    uo.orgid = Integer.parseInt((String) item);
			    uo.group = group;
			    uo.status = "0";
			    uo.fromTo = "2";
			    uriDo.uriOrg.add(uo);
			}
		    }
		    break;

		default:
		    break;
		}
	    }
	}
    }

    private EasType getEasType(EaSecured[] eases) {
	boolean havaDb = false;
	for (EaSecured eaSecured : eases) {
	    if (eaSecured.type() == EasType.DATABASE_ONLY)
		havaDb = true;
	    // TODO 遍历所有配置项，全部为默认配置时，使用数据库
	    if ("".equals(eaSecured.value()) && "".equals(eaSecured.org()) && eaSecured.IP().length == 0)
		havaDb = true;
	}
	if (havaDb) {
	    return EasType.DATABASE_ONLY;
	} else {
	    return EasType.SOURCE_ONLY;
	}
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
		    if (item.orgid == uo.orgid && item.group == uo.group) {
			item.id = uo.id;
			item.uriid = uo.uriid;
			item.status = uo.status;
			break;
		    }
		}
	    });
    }
}
