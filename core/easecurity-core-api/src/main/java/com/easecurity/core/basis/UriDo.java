/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.au.UriIp;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.re.UriEnum.*;
import com.easecurity.core.basis.au.UriIpEnum;
import com.easecurity.core.basis.au.UriOrgEnum;
import com.easecurity.core.basis.re.Uri;
import com.easecurity.util.JsonUtils;

/**
 * domain：接口（服务）
 */
public class UriDo implements Serializable {

    private static final long serialVersionUID = -7808950507994121828L;

    /**
     * 接口（服务）
     */
    public Uri uri;
    /**
     * 有接口（服务）权限的IPs
     */
    public UriIp uriIp;
    /**
     * 有接口（服务）权限的组织
     */
    public List<UriOrg> uriOrg;
    /**
     * 接口（服务）的权限规则组的数量
     */
    private int maxGroup = -1;

    private String _auOrgStr;

    /**
     * 从组织判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    // TODO 需要优化，临时凑合着写的
    public boolean havePermissionByOrgId(String orgId, int group) {
	if (uriOrg != null && !uriOrg.isEmpty()) {
	    if (_auOrgStr == null) {
		_auOrgStr = ",";
		for (UriOrg uo : uriOrg) {
		    if (uo.status == UriOrgEnum.Status.ENABLED)
			_auOrgStr += uo.getGroup1() + "." + uo.orgId + ",";
		}
	    }
	    return _auOrgStr.indexOf("," + group + "." + orgId + ",") > -1;
	} else
	    return false;
    }

    /**
     * 从组织判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByIp(String clientIp, int group) {
	if (uriIp != null && uriIp.status == UriIpEnum.Status.ENABLED && uriIp.ips != null && !uriIp.ips.isEmpty() && (group == 0 || group == uriIp.group1)) {
	    return uriIp.ips.indexOf(clientIp) > -1;
	} else
	    return false;
    }

    /**
     * 获取接口（服务）的权限规则组的数量
     */
    // TODO 遍历所有属性
    public int getMaxGroup() {
	if (maxGroup == -1) { // 初始化
	    if (uriIp != null)
		maxGroup = uriIp.getGroup1();
	    if (uriOrg != null) {
		uriOrg.forEach((it) -> {
		    if (it.group1 != null && it.group1 > maxGroup)
			maxGroup = it.group1;
		});
	    }
	}
	return maxGroup;
    }

    /**
     * 新建本地配置
     */
    // TODO 遍历属性
    public static UriDo createLocaleUriDo(EaSecured[] eases, String uri, String classFullName, String methodName, String methodSignature) {
	UriDo uriDo = new UriDo();
	Uri u = new Uri();
	u.id = -1; // 还没有存储在数据库中的数据，ID全部为-1
	u.uri = uri;
	u.classFullName = classFullName;
	u.methodName = methodName;
	u.methodSignature = methodSignature;
	u.easType = getEasType(eases);
	u.fromTo = FromTo.SOURCECODE;
	u.status = Status.ENABLED;
	uriDo.uri = u;
	for (int i = 0; i < eases.length; i++) {
	    EaSecured eas = eases[i];
	    createLocaleUriOrg(uriDo, eas.org(), i + 1);
	    createLocaleUriIp(uriDo, eas.IP(), i + 1);
	}
	return uriDo;
    }

    /**
     * 新建本地ip配置
     */
    private static void createLocaleUriIp(UriDo uriDo, String[] ips, int group) {
	if (ips != null && ips.length > 0) {
	    UriIp uip = new UriIp();
	    uip.uriId = uriDo.uri.id;
	    uip.status = UriIpEnum.Status.ENABLED;
	    uip.fromTo = UriIpEnum.FromTo.SOURCECODE;
	    uip.group1 = group;
	    uip.ips = "";
	    for (String string : ips) {
		uip.ips += string + ",";
	    }
	    uip.ips = uip.ips.substring(0, uip.ips.length() - 1);
	    uriDo.uriIp = uip;
	}
    }

    /**
     * 新建本地组织配置
     */
    // TODO 遍历属性
    @SuppressWarnings("unchecked")
    private static void createLocaleUriOrg(UriDo uriDo, String org, int group) {
	if (!org.isEmpty()) {
	    uriDo.uriOrg = new ArrayList<>();
	    Map<String, Object> allOrgs = (Map<String, Object>) JsonUtils.jsonToObject(org);
	    for (String k : allOrgs.keySet()) {
		Object v = allOrgs.get(k);
		switch (k) {
		case "id":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.orgId = Integer.parseInt((String) v);
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.orgId = Integer.parseInt((String) item);
			    uriDo.uriOrg.add(uo);
			}
		    }
		    break;
		case "code":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.code = (String) v;
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.code = (String) item;
			    uriDo.uriOrg.add(uo);
			}
		    }
		    break;
		case "name":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.name = (String) v;
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.name = (String) item;
			    uriDo.uriOrg.add(uo);
			}
		    }
		    break;
		case "fullCode":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.fullCode = (String) v;
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.fullCode = (String) item;
			    uriDo.uriOrg.add(uo);
			}
		    }
		    break;
		case "fullName":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.fullName = (String) v;
			uriDo.uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.fullName = (String) item;
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

    private static UriOrg newLocaleUriOrg(UriDo uriDo, int group) {
	UriOrg uo = new UriOrg();
	uo.uriId = uriDo.uri.id;
	uo.group1 = group;
	uo.status = UriOrgEnum.Status.ENABLED;
	uo.fromTo = UriOrgEnum.FromTo.SOURCECODE;
	return uo;
    }

    private static EasType getEasType(EaSecured[] eases) {
	boolean havaDb = false;
	for (EaSecured eaSecured : eases) {
	    if (eaSecured.type() == EasType.DATABASE_ONLY)
		havaDb = true;
	    // 遍历所有配置项，全部为默认配置时，使用数据库
	    if ("".equals(eaSecured.value()) && "".equals(eaSecured.org()) && "".equals(eaSecured.posts()) && "".equals(eaSecured.user()) && "".equals(eaSecured.role())
		    && "".equals(eaSecured.roleGroup()) && eaSecured.IP().length == 0 && eaSecured.type() == EasType.DEFAULT) {
		havaDb = true;
	    }
	}
	if (havaDb) {
	    return EasType.DATABASE_ONLY;
	} else {
	    return EasType.SOURCE_ONLY;
	}
    }
}
