/**
 * Copyright © 2021-2050 刘路峰版权所有。
 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.au.*;
import com.easecurity.core.basis.re.UriEnum.*;
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
     * 有接口（服务）权限的账号
     */
    public List<UriUser> uriUser;
    /**
     * 有接口（服务）权限的组织
     */
    public List<UriOrg> uriOrg;
    /**
     * 有接口（服务）权限的职务
     */
    public List<UriPosts> uriPosts;
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

    private Map<String, List<String>> _uriRoleUser = null;
    private Map<String, List<String>> _uriRoleOrg = null;
    private Map<String, List<String>> _uriRolePosts = null;
    private Map<String, List<String>> _uriRoleGroup = null;
    private Map<String, List<String>> _uriRole = null;

    /**
     * 从账号判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByUser(Map<String, String> user, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (uriUser != null && !uriUser.isEmpty()) {
	    if (_uriRoleUser == null) {
		_uriRoleUser = new HashMap<>();
		for (UriUser uu : uriUser) {
		    if (uu.status == UriUserEnum.Status.ENABLED) {
			permissions(_uriRoleUser, uu.userId, uu.annotation, uu.getGroup1());
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	return havePermission(user, _uriRoleUser, group);
    }

    /**
     * 从组织判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByOrg(Map<String, String> org, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (uriOrg != null && !uriOrg.isEmpty()) {
	    if (_uriRoleOrg == null) {
		_uriRoleOrg = new HashMap<>();
		for (UriOrg uo : uriOrg) {
		    if (uo.status == UriOrgEnum.Status.ENABLED) {
			permissions(_uriRoleOrg, uo.orgId, uo.annotation, uo.getGroup1());
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	return havePermission(org, _uriRoleOrg, group);
    }

    /**
     * 从职务判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByPosts(Map<String, String> posts, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (uriPosts != null && !uriPosts.isEmpty()) {
	    if (_uriRolePosts == null) {
		_uriRolePosts = new HashMap<>();
		for (UriPosts up : uriPosts) {
		    if (up.status == UriPostsEnum.Status.ENABLED) {
			permissions(_uriRolePosts, up.postsId, up.annotation, up.getGroup1());
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	return havePermission(posts, _uriRolePosts, group);
    }

    /**
     * 从小角色判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByRole(Map<String, String> role, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (_uriRole != null && !_uriRole.isEmpty()) {
	    if (_uriRole == null) {
		_uriRole = new HashMap<>();
		for (UriRole rl : uriRole) {
		    if (rl.status == UriRoleEnum.Status.ENABLED) {
			permissions(_uriRole, rl.roleId, rl.annotation, rl.getGroup1());
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	return havePermission(role, _uriRole, group);
    }

    /**
     * 从大角色判断是否拥有此接口的权限。
     *
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByRoleGroup(Map<String, String> roleGroup, int group) {
	// 初始化配置，如果无此配置项，则直接返回无权限
	if (uriRoleGroup != null && !uriRoleGroup.isEmpty()) {
	    if (_uriRoleGroup == null) {
		_uriRoleGroup = new HashMap<>();
		for (UriRoleGroup rg : uriRoleGroup) {
		    if (rg.status == UriRoleGroupEnum.Status.ENABLED) {
			permissions(_uriRoleGroup, rg.roleGroupId, rg.annotation, rg.getGroup1());
		    }
		}
	    }
	} else
	    return false;

	// 有配置项时，校验是否有满足配置项的要求
	return havePermission(roleGroup, _uriRoleGroup, group);
    }

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
    public int getMaxGroup() {
	if (maxGroup == -1) { // 初始化
	    if (uriIp != null)
		maxGroup = uriIp.getGroup1();
	    if (uriUser != null) {
		uriUser.forEach((it) -> {
		    if (it.group1 != null && it.group1 > maxGroup)
			maxGroup = it.group1;
		});
	    }
	    if (uriOrg != null) {
		uriOrg.forEach((it) -> {
		    if (it.group1 != null && it.group1 > maxGroup)
			maxGroup = it.group1;
		});
	    }
	    if (uriPosts != null) {
		uriPosts.forEach((it) -> {
		    if (it.group1 != null && it.group1 > maxGroup)
			maxGroup = it.group1;
		});
	    }
	    if (uriRole != null) {
		uriRole.forEach((it) -> {
		    if (it.group1 != null && it.group1 > maxGroup)
			maxGroup = it.group1;
		});
	    }
	    if (uriRoleGroup != null) {
		uriRoleGroup.forEach((it) -> {
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
	    if (eas.IP() != null && eas.IP().length > 0) {
		if (uriDo.uriIp != null)
		    throw new RuntimeException("多组控制条件中，IP的控制条件应该放到某一组中");
		uriDo.uriIp = createLocaleUriIp(uriDo, eas.IP(), i + 1);
	    }
	    if (!eas.user().isEmpty()) {
		if (uriDo.uriUser == null)
		    uriDo.uriUser = new ArrayList<>();
		uriDo.uriUser.addAll(createLocaleUriUser(uriDo, eas.user(), i + 1));
	    }
	    if (!eas.org().isEmpty()) {
		if (uriDo.uriOrg == null)
		    uriDo.uriOrg = new ArrayList<>();
		uriDo.uriOrg.addAll(createLocaleUriOrg(uriDo, eas.org(), i + 1));
	    }
	    if (!eas.posts().isEmpty()) {
		if (uriDo.uriPosts == null)
		    uriDo.uriPosts = new ArrayList<>();
		uriDo.uriPosts.addAll(createLocaleUriPosts(uriDo, eas.posts(), i + 1));
	    }
	    if (!eas.role().isEmpty()) {
		if (uriDo.uriRole == null)
		    uriDo.uriRole = new ArrayList<>();
		uriDo.uriRole.addAll(createLocaleUriRole(uriDo, eas.role(), i + 1));
	    }
	    if (!eas.roleGroup().isEmpty()) {
		if (uriDo.uriRoleGroup == null)
		    uriDo.uriRoleGroup = new ArrayList<>();
		uriDo.uriRoleGroup.addAll(createLocaleUriRoleGroup(uriDo, eas.roleGroup(), i + 1));
	    }
	}
	// 匿名注解
	if (easAnonymous != null) {
	    int aGroup = eases.length + 1;
	    if (uriDo.uriRoleGroup == null)
		uriDo.uriRoleGroup = new ArrayList<>();
	    uriDo.uriRoleGroup.addAll(createLocaleUriRoleGroup(uriDo, "{'code':['anonymous']}", aGroup));
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
     * 新建本地账号配置
     */
    private static List<UriUser> createLocaleUriUser(UriDo uriDo, String user, int group) {
	if (!user.isEmpty()) {
	    List<UriUser> uriUser = new ArrayList<>();
	    UriUser uu = newLocaleUriUser(uriDo, group);
	    uu.annotation = user;
	    uriUser.add(uu);
	    return uriUser;
	}
	return null;
    }
    
    /**
     * 新建本地组织配置
     */
    private static List<UriOrg> createLocaleUriOrg(UriDo uriDo, String org, int group) {
	if (!org.isEmpty()) {
	    List<UriOrg> uriOrg = new ArrayList<>();
	    UriOrg uo = newLocaleUriOrg(uriDo, group);
	    uo.annotation = org;
	    uriOrg.add(uo);
	    return uriOrg;
	}
	return null;
    }

    /**
     * 新建本地职务配置
     */
    private static List<UriPosts> createLocaleUriPosts(UriDo uriDo, String posts, int group) {
	if (!posts.isEmpty()) {
	    List<UriPosts> uriPosts = new ArrayList<>();
	    UriPosts up = newLocaleUriPosts(uriDo, group);
	    up.annotation = posts;
	    uriPosts.add(up);
	    return uriPosts;
	}
	return null;
    }

    private static UriUser newLocaleUriUser(UriDo uriDo, int group) {
	UriUser uu = new UriUser();
	uu.uriId = uriDo.uri.id;
	uu.group1 = group;
	uu.status = UriUserEnum.Status.ENABLED;
	uu.fromTo = UriUserEnum.FromTo.SOURCECODE;
	return uu;
    }
    
    private static UriOrg newLocaleUriOrg(UriDo uriDo, int group) {
	UriOrg uo = new UriOrg();
	uo.uriId = uriDo.uri.id;
	uo.group1 = group;
	uo.status = UriOrgEnum.Status.ENABLED;
	uo.fromTo = UriOrgEnum.FromTo.SOURCECODE;
	return uo;
    }
    
    private static UriPosts newLocaleUriPosts(UriDo uriDo, int group) {
	UriPosts up = new UriPosts();
	up.uriId = uriDo.uri.id;
	up.group1 = group;
	up.status = UriPostsEnum.Status.ENABLED;
	up.fromTo = UriPostsEnum.FromTo.SOURCECODE;
	return up;
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void permissions(Map<String, List<String>> pMap, Integer id, String annotation, int group) {
	if (id != null) {
	    if (!pMap.containsKey("id"))
		pMap.put("id", new ArrayList<String>());
	    pMap.get("id").add(group + "." + id);
	}
	if (annotation != null && !annotation.trim().isEmpty()) {
	    Map<String, Object> tan = (Map<String, Object>) JsonUtils.jsonToObject(annotation);
	    for (String k : tan.keySet()) {
		if (!pMap.containsKey(k))
		    pMap.put(k, new ArrayList<String>());
		Object v = tan.get(k);
		if (v instanceof String)
		    pMap.get(k).add(group + "." + v);
		if (v instanceof List)
		    ((List) v).forEach((it) -> {
			pMap.get(k).add(group + "." + it);
		    });
	    }
	}
    }

    private boolean havePermission(Map<String, String> user, Map<String, List<String>> pMap, int group) {
	for (String k : user.keySet()) {
	    if (pMap.containsKey(k)) {
		String[] vs = user.get(k).split(",");
		for (String v : vs) {
		    if (pMap.get(k).indexOf(group + "." + v) > -1)
			return true;
		}
	    }
	}
	return false;
    }
}
