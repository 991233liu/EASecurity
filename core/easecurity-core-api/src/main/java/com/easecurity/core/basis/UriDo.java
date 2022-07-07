/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.au.UriIp;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.re.UriEnum.*;
import com.easecurity.core.basis.au.UriIpEnum;
import com.easecurity.core.basis.au.UriOrgEnum;
import com.easecurity.core.basis.au.UriRoleEnum;
import com.easecurity.core.basis.au.UriRoleGroup;
import com.easecurity.core.basis.au.UriRoleGroupEnum;
import com.easecurity.core.basis.au.UriRole;
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
     * 有接口（服务）权限的小角色
     */
    public List<UriRole> uriRole;
    /**
     * 有接口（服务）权限的大角色
     */
    public List<UriRoleGroup> uriRoleGroup;
    /**
     * 接口（服务）的权限规则组的数量
     */
    private int maxGroup = -1;

    private String _auOrgIdStr;
    private String _auOrgCodeStr;
    private String _auOrgNameStr;
    private String _auOrgFullCodeStr;
    private String _auOrgFullNameStr;

    /**
     * 从组织判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByOrg(Map<String, String> org, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (uriOrg != null && !uriOrg.isEmpty()) {
	    if (_auOrgIdStr == null) {
		_auOrgIdStr = ",";
		_auOrgCodeStr = ",";
		_auOrgNameStr = ",";
		_auOrgFullCodeStr = ",";
		_auOrgFullNameStr = ",";
		for (UriOrg uo : uriOrg) {
		    if (uo.status == UriOrgEnum.Status.ENABLED) {
			if (uo.orgId != null)
			    _auOrgIdStr += uo.getGroup1() + "." + uo.orgId + ",";
			if (uo.code != null && !uo.code.trim().isEmpty())
			    _auOrgCodeStr += uo.getGroup1() + "." + uo.code + ",";
			if (uo.name != null && !uo.name.trim().isEmpty())
			    _auOrgNameStr += uo.getGroup1() + "." + uo.name + ",";
			if (uo.fullCode != null && !uo.fullCode.trim().isEmpty())
			    _auOrgFullCodeStr += uo.getGroup1() + "." + uo.fullCode + ",";
			if (uo.fullName != null && !uo.fullName.trim().isEmpty())
			    _auOrgFullNameStr += uo.getGroup1() + "." + uo.fullName + ",";
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	// TODO 不对，改了一半，以后再说
	if (isDirty()) { // 与SecurityCentre进行配置同步后，配置信息全部转换为了关联关系，直接使用id即可
	    for (String k : org.keySet()) {
		String[] vs = org.get(k).split(",");
		for (String v : vs) {
		    switch (k) {
		    case "id":
			if (_auOrgIdStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "code":
			if (_auOrgCodeStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "name":
			if (_auOrgNameStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "fullCode":
			if (_auOrgFullCodeStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "fullName":
			if (_auOrgFullNameStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    default:
			break;
		    }
		}
	    }
	} else { // 服务刚刚启动，还没有同步时，使用代码中的配置验证
	    for (String k : org.keySet()) {
		String[] vs = org.get(k).split(",");
		for (String v : vs) {
		    switch (k) {
		    case "id":
			if (_auOrgIdStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "code":
			if (_auOrgCodeStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "name":
			if (_auOrgNameStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "fullCode":
			if (_auOrgFullCodeStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    case "fullName":
			if (_auOrgFullNameStr.indexOf("," + group + "." + v + ",") > -1) {
			    return true;
			}
			break;
		    default:
			break;
		    }
		}
	    }
	}
	return false;
    }
//    public boolean havePermissionByOrgId(String orgId, int group) {
//	if (uriOrg != null && !uriOrg.isEmpty()) {
//	    if (_auOrgStr == null) {
//		_auOrgStr = ",";
//		for (UriOrg uo : uriOrg) {
//		    if (uo.status == UriOrgEnum.Status.ENABLED)
//			_auOrgStr += uo.getGroup1() + "." + uo.orgId + ",";
//		}
//	    }
//	    return _auOrgStr.indexOf("," + group + "." + orgId + ",") > -1;
//	} else
//	    return false;
//    }

    /**
     * 从IP判断是否拥有此接口的权限。
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
     * 是否为脏数据。当服务刚刚启动，还没有与SecurityCentre进行配置同步时，此时返回false。
     */
    public boolean isDirty() {
	return !(uri == null || uri.id == -1);
    }

    /**
     * 新建本地配置
     */
    // TODO 遍历属性
    public static UriDo createLocaleUriDo(EaSecured[] eases, EaSecuredAnonymous easAnonymous, String uri, String classFullName, String methodName, String methodSignature) {
	if (eases == null)
	    eases = new EaSecured[] {};
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
	// 普通注解
	for (int i = 0; i < eases.length; i++) {
	    EaSecured eas = eases[i];
	    uriDo.uriOrg = createLocaleUriOrg(uriDo, eas.org(), i + 1);
	    uriDo.uriIp = createLocaleUriIp(uriDo, eas.IP(), i + 1);
	    uriDo.uriRole = createLocaleUriRole(uriDo, eas.role(), i + 1);
	    uriDo.uriRoleGroup = createLocaleUriRoleGroup(uriDo, eas.roleGroup(), i + 1);
	}
	// 匿名注解
	if (easAnonymous != null) {
	    int aGroup = eases.length + 1;
	    if (uriDo.uriRoleGroup == null)
		uriDo.uriRoleGroup = new ArrayList<>();
	    uriDo.uriRoleGroup.addAll(createLocaleUriRoleGroup(uriDo, "'code':['anonymous']}", aGroup));
	}
	return uriDo;
    }

    /**
     * 新建本地Role配置
     */
    private static List<UriRole> createLocaleUriRole(UriDo uriDo, String role, int group) {
	if (!role.isEmpty()) {
	    List<UriRole> uriRoles = new ArrayList<>(1);
	    UriRole uriRole = newLocaleUriRole(uriDo, group);
	    uriRole.annotation = role;
	    uriRoles.add(uriRole);
	    return uriRoles;
	}
	return null;
    }

    /**
     * 新建本地RoleGroup配置
     */
    private static List<UriRoleGroup> createLocaleUriRoleGroup(UriDo uriDo, String roleGroup, int group) {
	if (!roleGroup.isEmpty()) {
	    List<UriRoleGroup> uriRoleGroups = new ArrayList<>(1);
	    UriRoleGroup uriRoleGroup = newLocaleUriRoleGroup(uriDo, group);
	    uriRoleGroup.annotation = roleGroup;
	    uriRoleGroups.add(uriRoleGroup);
	    return uriRoleGroups;
	}
	return null;
    }

    /**
     * 新建本地ip配置
     */
    private static UriIp createLocaleUriIp(UriDo uriDo, String[] ips, int group) {
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
	    return uip;
	}
	return null;
    }

    /**
     * 新建本地组织配置
     */
    // TODO 遍历属性
    @SuppressWarnings("unchecked")
    private static List<UriOrg> createLocaleUriOrg(UriDo uriDo, String org, int group) {
	if (!org.isEmpty()) {
	    List<UriOrg> uriOrg = new ArrayList<>();
	    Map<String, Object> allOrgs = (Map<String, Object>) JsonUtils.jsonToObject(org);
	    for (String k : allOrgs.keySet()) {
		Object v = allOrgs.get(k);
		switch (k) {
		case "id":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.orgId = Integer.parseInt((String) v);
			uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.orgId = Integer.parseInt((String) item);
			    uriOrg.add(uo);
			}
		    }
		    break;
		case "code":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.code = (String) v;
			uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.code = (String) item;
			    uriOrg.add(uo);
			}
		    }
		    break;
		case "name":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.name = (String) v;
			uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.name = (String) item;
			    uriOrg.add(uo);
			}
		    }
		    break;
		case "fullCode":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.fullCode = (String) v;
			uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.fullCode = (String) item;
			    uriOrg.add(uo);
			}
		    }
		    break;
		case "fullName":
		    if (v instanceof String) {
			UriOrg uo = newLocaleUriOrg(uriDo, group);
			uo.fullName = (String) v;
			uriOrg.add(uo);
		    } else {
			for (String item : (List<String>) v) {
			    UriOrg uo = newLocaleUriOrg(uriDo, group);
			    uo.fullName = (String) item;
			    uriOrg.add(uo);
			}
		    }
		    break;

		default:
		    break;
		}
	    }
	    return uriOrg;
	}
	return null;
    }

    private static UriOrg newLocaleUriOrg(UriDo uriDo, int group) {
	UriOrg uo = new UriOrg();
	uo.uriId = uriDo.uri.id;
	uo.group1 = group;
	uo.status = UriOrgEnum.Status.ENABLED;
	uo.fromTo = UriOrgEnum.FromTo.SOURCECODE;
	return uo;
    }

    private static UriRole newLocaleUriRole(UriDo uriDo, int group) {
	UriRole uriRole = new UriRole();
	uriRole.uriId = uriDo.uri.id;
	uriRole.group1 = group;
	uriRole.status = UriRoleEnum.Status.ENABLED;
	uriRole.fromTo = UriRoleEnum.FromTo.SOURCECODE;
	return uriRole;
    }

    private static UriRoleGroup newLocaleUriRoleGroup(UriDo uriDo, int group) {
	UriRoleGroup uriRoleGroup = new UriRoleGroup();
	uriRoleGroup.uriId = uriDo.uri.id;
	uriRoleGroup.group1 = group;
	uriRoleGroup.status = UriRoleGroupEnum.Status.ENABLED;
	uriRoleGroup.fromTo = UriRoleGroupEnum.FromTo.SOURCECODE;
	return uriRoleGroup;
    }

    // TODO 逻辑变化了，需要重构

    private static EasType getEasType(EaSecured[] eases) {
	boolean havaSR = false;
	boolean havaDb = false;
	// 遍历所有配置项，全部为DEFAULT时，为DEFAULT
	// 有1个为SOURCE_ONLY时，为SOURCE_ONLY
	// 其它为DATABASE_ONLY
	for (EaSecured eaSecured : eases) {
	    if (eaSecured.type() == EasType.DATABASE_ONLY)
		havaDb = true;
	    if (eaSecured.type() == EasType.SOURCE_ONLY)
		havaSR = true;
	}
	if (havaSR) {
	    return EasType.SOURCE_ONLY;
	} else if (havaDb) {
	    return EasType.DATABASE_ONLY;
	} else {
	    return EasType.DEFAULT;
	}
    }
}
