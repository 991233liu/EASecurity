/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.access.annotation.EaSecureds;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.core.basis.UriDo;
import com.easecurity.core.basis.au.UriOrg;

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
			lUriDo = UriDo.createLocaleUriDo(eases, uri, classFullName, methodName, methodSignature);
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
     * 获取Method上的EaSecured配置
     * 
     * @param method
     * @return
     */
    public EaSecured[] getEaSecuredWithoutAnonymous(Method method) {
	EaSecured[] teases = null;
	EaSecureds meases = method.getAnnotation(EaSecureds.class);
	EaSecured meas = method.getAnnotation(EaSecured.class);
	// 如果类和方法同时配置了@EaSecured，则使用方法的安全配置。
	if (meases != null) {
	    teases = meases.value();
	} else if (meas != null) {
	    teases = new EaSecured[] { meas };
	} else {
	    EaSecureds eases = method.getDeclaringClass().getAnnotation(EaSecureds.class);
	    EaSecured eas = method.getDeclaringClass().getAnnotation(EaSecured.class);
	    if (eases != null) {
		teases = eases.value();
	    } else if (eas != null) {
		teases = new EaSecured[] { eas };
	    }
	}
	return teases;
    }

    /**
     * 判断Method是否受EaSecured注解管理的方法
     * 
     * @param method
     * @return
     */
    public boolean isEaSecured(Method method) {
	// 如果类和方法同时配置了@EaSecured，则使用方法的安全配置。
	// Method上的控制器
	if (method.isAnnotationPresent(EaSecured.class) || method.isAnnotationPresent(EaSecureds.class) || method.isAnnotationPresent(EaSecuredAnonymous.class)) {
	    return true;
	}
	// 类上的控制器
	if (method.getDeclaringClass().isAnnotationPresent(EaSecured.class) || method.getDeclaringClass().isAnnotationPresent(EaSecureds.class)
		|| method.getDeclaringClass().isAnnotationPresent(EaSecuredAnonymous.class)) {
	    return true;
	}
	return false;
    }

    /**
     * 判断Method是否EaSecuredAnonymous注解方法
     * 
     * @param method
     * @return
     */
    public boolean isEaSecuredAnonymous(Method method) {
	// 如果类和方法同时配置了@EaSecured，则使用方法的安全配置。
	// Method上的控制器
	if (method.isAnnotationPresent(EaSecuredAnonymous.class)) {
	    return true;
	}
	if (method.isAnnotationPresent(EaSecured.class) || method.isAnnotationPresent(EaSecureds.class)) {
	    return false;
	}
	// 类上的控制器
	if (method.getDeclaringClass().isAnnotationPresent(EaSecuredAnonymous.class)) {
	    return true;
	}
	return false;
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
	Map<String, Map<String, String>> allIdentities = new HashMap<>();
	if (userDt != null)
	    allIdentities = userDt.allIdentitiesWithMap();
	return havePermission(uriDo, group, allIdentities, uri, clientIp);
    }

    /**
     * 校验访问权限（纯数据库模式）
     */
    public boolean validation(String uri, UserDetails userDt, String clientIp) {
	uriDos = accessRegister.getAllUriDos();
	Map<String, Map<String, String>> allIdentities = new HashMap<>();
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
	    if (!havePermission(uriDo, i + 1, allIdentities, uri, clientIp)) {
		validation = false;
		break;
	    }
	}
	return validation;
    }

    // TODO 遍历权限，判断每种情况
    private boolean havePermission(UriDo uriDo, int group, Map<String, Map<String, String>> allIdentities, String uri, String clientIp) {
	if (uriDo.havePermissionByIp(clientIp, group)) {
	    return true;
	} else {
	    for (String k : allIdentities.keySet()) {
		switch (k) {
		case "user":
		    break;
		case "org":
		    if (uriDo.havePermissionByOrg(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		}
	    }
	    return false;
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
		    if (item.orgId == uo.orgId && item.group1 == uo.group1) {
			item.id = uo.id;
			item.uriId = uo.uriId;
			item.status = uo.status;
			break;
		    }
		}
	    });
    }
}
