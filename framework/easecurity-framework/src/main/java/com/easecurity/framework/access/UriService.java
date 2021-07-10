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
import com.easecurity.core.basis.UriDo;
import com.easecurity.core.basis.UserDo;
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

    protected UriService(AccessRegister accessRegister) {
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
    // TODO 传输、存储……好多问题
    public void saveUriPermissions(EaSecured eas, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo lUriDo = null;
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
	// TODO 每次都取一次吗？？？
	uriDos = accessRegister.getAllUriDos();
	if (uriDos.containsKey(uri)) {
	    // TODO 如果已经存在了，需要更新吗？？？
	    EasType type = eas.type();
	    if (type != EasType.DATABASE_ONLY && System.currentTimeMillis() > lastModifyTime) { // 使用源码中的配置时，使用数据库中的状态信息，每分钟更新一次
		UriDo uriDo = uriDos.get(uri);
		synchronized (this) {
		    updateLocaleUriDoStatus(lUriDo, uriDo);
		    lastModifyTime = System.currentTimeMillis() + 60000;
		}
	    }
	} else { // 不存在时，则新建配置
	    // TODO 怎么转化好呢？还是先从查开始吧，存储应该方便查询使用
	    // TODO 每次启动更新一次？？？
	}
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured eas, String uri, UserDo userDo) {
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
			if (uriDo.havePermissionThroughOrgId(id)) {
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

    // TODO 新建，遍历属性
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
	uriDo.uri = u;
	String org = eas.org();
	if (!org.isEmpty()) {
	    uriDo.uriOrg = new ArrayList<>();
	    Map<String, Object> allOrgs = (Map<String, Object>) JsonUtils.jsonToObject(org);
	    allOrgs.forEach((k, v) -> {
		// TODO 遍历，其它方式
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

    // TODO 从数据库汇总更新本地状态
    private void updateLocaleUriDoStatus(UriDo lUriDo, UriDo uriDo) {

    }
}
