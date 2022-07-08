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

/**
 * 接口（访问）访问控制服务
 *
 */
public class UriService {
    private static final Logger log = LoggerFactory.getLogger(UriService.class);

    AccessRegister accessRegister = null;

    private static volatile Map<String, UriDo> sourceUriDos = new HashMap<>();

    public UriService(AccessRegister accessRegister) {
	this.accessRegister = accessRegister;
    }

    /**
     * 获取URI的授权配置信息
     * 
     * @param uri
     * @return
     */
    public UriDo getUriDo(String uri) {
	Map<String, UriDo> uriDos = accessRegister.getAllUriDos();
	// TODO 刚启动时uriDos为null，需要处理

	if (uriDos != null && uriDos.containsKey(uri))
	    return uriDos.get(uri);
	// 开发模式下，如果在SecurityCentre中没有查到相关配置，就使用源码中的配置
	if (accessRegister.getEaSecurityConfiguration().isDevelopmentMode())
	    return getSourceUriDoUriDo(uri);
	else
	    return null;
    }

    /**
     * 从源码获取URI的授权配置信息，此信息未与服务器同步
     * 
     * @param uri
     * @return
     */
    public UriDo getSourceUriDoUriDo(String uri) {
	if (sourceUriDos.containsKey(uri))
	    return sourceUriDos.get(uri);
	return null;
    }

    /**
     * 更新URI的授权信息。如果URI已经存在，则更新状态以外其它信息；如果不存在则新建
     * 
     * @param eas
     * @param easAnonymous
     * @param uri
     * @param classFullName
     * @param methodName
     * @param methodSignature
     */
    public void saveUriPermissions(EaSecured[] eases, EaSecuredAnonymous easAnonymous, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo sUriDo = null;
	UriDo uriDo = null;
	try {
	    if (!sourceUriDos.containsKey(uri)) { // 启动后第一次被访问
		synchronized (this) {
		    if (!sourceUriDos.containsKey(uri)) {
			sUriDo = UriDo.createLocaleUriDo(eases, easAnonymous, uri, classFullName, methodName, methodSignature);
			sourceUriDos.put(uri, sUriDo);
			accessRegister.saveUriEas(sUriDo);
		    }
		}
	    }
	} catch (Exception e) {
	    log.error("更新URI的授权信息时出现异常：" + sUriDo + "==" + uriDo, e);
	}
    }

    /**
     * 获取EaSecured配置
     * 
     * @param method
     * @return
     */
    public EaSecured[] getEaSecuredWithoutAnonymous(Method method) {
	EaSecured[] teases = null;
	EaSecureds meases = method.getAnnotation(EaSecureds.class);
	EaSecured meas = method.getAnnotation(EaSecured.class);
	// 如果类和方法同时配置了@EaSecured或者@EaSecuredAnonymous，则使用方法的安全配置。
	if (meases != null) {
	    teases = meases.value();
	} else if (meas != null) {
	    teases = new EaSecured[] { meas };
	} else if (!method.isAnnotationPresent(EaSecuredAnonymous.class)) {
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
	// 如果类和方法同时配置了@EaSecured或者@EaSecuredAnonymous，则使用方法的安全配置。
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
     * 判断是否EaSecuredAnonymous注解方法
     * 
     * @param method
     * @return
     */
    public boolean isEaSecuredAnonymous(Method method) {
	// 如果类和方法同时配置了@EaSecured或者@EaSecuredAnonymous，则使用方法的安全配置。
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
    public boolean validation(EaSecured[] eases, EaSecuredAnonymous easAnonymous, String uri, UserDetails userDt, String clientIp) {
	if (eases == null)
	    eases = new EaSecured[] {};
	// 多组为and关系，有一个false则false
	for (int i = 0; i < eases.length; i++) {
	    if (!validation(eases[i], i + 1, uri, userDt, clientIp)) {
		return false;
	    }
	}
	if (easAnonymous != null) {
	    return true;
	}
	return true;
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured eas, int group, String uri, UserDetails userDt, String clientIp) {
	UriDo uriDo = getUriDo(uri);
	Map<String, Map<String, String>> allIdentities = new HashMap<>();
	if (userDt != null)
	    allIdentities = userDt.allIdentitiesWithMap();
	return havePermission(uriDo, group, allIdentities, uri, clientIp);
    }

    /**
     * 校验访问权限（纯数据库模式）
     */
    public boolean validation(String uri, UserDetails userDt, String clientIp) {
	UriDo uriDo = getUriDo(uri);
	Map<String, Map<String, String>> allIdentities = new HashMap<>();
	if (userDt != null)
	    allIdentities = userDt.allIdentitiesWithMap();
	// 多组为and关系，有一个false则false
	for (int i = 0; i < uriDo.getMaxGroup(); i++) {
	    if (!havePermission(uriDo, i + 1, allIdentities, uri, clientIp)) {
		return false;
	    }
	}
	return true;
    }

    private boolean havePermission(UriDo uriDo, int group, Map<String, Map<String, String>> allIdentities, String uri, String clientIp) {
	if (uriDo == null) {
	    if (accessRegister.getEaSecurityConfiguration().isDevelopmentMode()) { // 开发模式下
		log.error("权限校验失败。发现了一个不存在的Uri：{}", uri);
	    } else { // 非开发模式下
		throw new NullPointerException("发现了一个不存在的UriDo，请联系管理员，URI为：" + uri);
	    }
	}
	// 非开发模式下：当服务刚刚启动，还没有与SecurityCentre进行配置同步或者同步失败时，不能执行
	if (uriDo.isDirty() && uriDo.uri.easType != EasType.SOURCE_ONLY && !accessRegister.getEaSecurityConfiguration().isDevelopmentMode()) {
	    throw new NullPointerException("授权配置没有更新，请检查SecurityCentre是否启动，且网络已经通畅");
	}

	if (uriDo.havePermissionByIp(clientIp, group)) {
	    return true;
	} else {
	    for (String k : allIdentities.keySet()) {
		switch (k) {
		case "role":
		    if (uriDo.havePermissionByRole(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		case "roleGroup":
		    if (uriDo.havePermissionByRoleGroup(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		case "org":
		    if (uriDo.havePermissionByOrg(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		case "posts":
		    if (uriDo.havePermissionByPosts(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		case "user":
		    if (uriDo.havePermissionByUser(allIdentities.get(k), group)) {
			return true;
		    }
		    break;
		}
	    }
	    return false;
	}
    }
}
