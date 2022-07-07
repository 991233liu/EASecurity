/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.au.UriIp;
import com.easecurity.core.basis.au.UriIpEnum;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.au.UriOrgEnum;
import com.easecurity.core.basis.au.UriRole;
import com.easecurity.core.basis.au.UriRoleEnum;
import com.easecurity.core.basis.au.UriRoleGroup;
import com.easecurity.core.basis.au.UriRoleGroupEnum;
import com.easecurity.core.basis.re.Uri;
import com.easecurity.core.redis.RedisUtil;

/**
 * 接口（访问）访问控制服务
 *
 */
@Service
public class UriService {
    private static final Logger log = LoggerFactory.getLogger(UriService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RedisUtil redisUtil;

    private static final Long URI_TIMEOUT = (long) (10 * 60 * 60); // 10小时Redis缓存失效

    private static volatile Map<String, UriDo> allUriDoMap = null;
    private static volatile long validTime = -1;

    private String sql = "SELECT * FROM re_uri";
    private String sql2 = "SELECT id FROM re_uri where uri = ?";
    private String sql3 = "INSERT INTO re_uri (uri, class_full_name, method_name, method_signature, eas_type, from_to, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private String sql4 = "UPDATE re_uri set class_full_name=?, method_name=?, method_signature=?, eas_type=?, from_to=? where id=?";

    private String sql10 = "SELECT * FROM au_uri_org where uri_id = ?";
    private String sql11 = "SELECT id FROM au_uri_org where uri_id = ? AND from_to=?";
    private String sql12 = "INSERT INTO au_uri_org (group1, code, uri_id, full_code, org_id, name, full_name, from_to, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    String sql4 = "UPDATE au_uri_org set from_to=? where id=?";

    private String sql20 = "SELECT * FROM au_uri_ip where uri_id = ?";
    private String sql21 = "SELECT id FROM au_uri_ip where uri_id = ? AND from_to=?";
    private String sql22 = "INSERT INTO au_uri_ip(group1, ips, uri_id, status, from_to) VALUES (?, ?, ?, ?, ?)";

    private String sql30 = "SELECT * FROM au_uri_role where uri_id = ?";
    private String sql31 = "SELECT id FROM au_uri_role where uri_id = ? AND from_to=?";
    private String sql32 = "INSERT INTO au_uri_role(annotation, group1, role_id, uri_id, status, from_to) VALUES (?, ?, ?, ?, ?, ?)";

    private String sql40 = "SELECT * FROM au_uri_role_group where uri_id = ?";
    private String sql41 = "SELECT id FROM au_uri_role_group where uri_id = ? AND from_to=?";
    private String sql42 = "INSERT INTO au_uri_role_group(annotation, group1, role_group_id, uri_id, status, from_to) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * 从core初始化URI信息。
     */
    @SuppressWarnings("unchecked")
    private synchronized Map<String, UriDo> loadAllFromCore(boolean force) {
	// 如果Redis中有缓存，则使用Redis中；如果没有，则加载数据库并缓存到Redis
	if (!force && redisUtil.hasKey("uri:rootList")) {
	    allUriDoMap = (Map<String, UriDo>) redisUtil.get("uri:all");
	} else {
	    Map<String, UriDo> audoAllMap = new HashMap<String, UriDo>();
	    List<Uri> all = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Uri.class));
	    for (Uri uri : all) {
		// TODO 属性没加载全
		UriDo udo = new UriDo();
		udo.uri = uri;
		udo.uriOrg = jdbcTemplate.query(sql10, new BeanPropertyRowMapper<>(UriOrg.class), uri.id);
		List<UriIp> uriIp = jdbcTemplate.query(sql20, new BeanPropertyRowMapper<>(UriIp.class), uri.id);
		if (!uriIp.isEmpty())
		    udo.uriIp = uriIp.get(0);
		udo.uriRole = jdbcTemplate.query(sql30, new BeanPropertyRowMapper<>(UriRole.class), uri.id);
		udo.uriRoleGroup = jdbcTemplate.query(sql40, new BeanPropertyRowMapper<>(UriRoleGroup.class), uri.id);
		audoAllMap.put(uri.uri, udo);
//		redisUtil.set("uri:" + uri.id, udo, URI_TIMEOUT);
	    }
	    redisUtil.set("uri:all", audoAllMap, URI_TIMEOUT);
	    allUriDoMap = audoAllMap;
	}
	validTime = System.currentTimeMillis() + (5 * 60 * 1000); // 内存中5分钟内有效
	log.info("-------## 加载的UriDo有：{}", allUriDoMap);
	return allUriDoMap;
    }

    /**
     * 获取所以URI信息。5分钟内使用内存中的数据，降低core的压力
     * 
     * @return
     */
    public Map<String, UriDo> getAllUriDos() {
	// 有效期内直接返回内存缓存的数据
	if (System.currentTimeMillis() < validTime)
	    return allUriDoMap;

	return loadAllFromCore(false);
    }

    /**
     * 保存UriDo配置。
     */
    // TODO 优化代码结构
    // TODO 开发模式下强制更新所有信息及策略？？？force状态可以从客户端header中传过来
    public void saveUriDo(UriDo lUriDo) {
	// 保存URI信息
	int uriId = saveUri(lUriDo.uri);

	// 保存授权信息
	// TODO bug，按模式更新的判断应该写在这里，而不是下级
	if (lUriDo.uri.easType == EasType.SOURCE_ONLY) { // 源码模式下，数据库中授权信息更新+新增。
	    saveUriOrg(lUriDo.uriOrg, uriId, true);
	    saveUriIp(lUriDo.uriIp, uriId, true);
	    saveUriRole(lUriDo.uriRole, uriId, true);
	    saveUriRoleGroup(lUriDo.uriRoleGroup, uriId, true);
	} else if (lUriDo.uri.easType == EasType.DATABASE_ONLY) { // 数据库模式下，数据库中授权信息不更新，只新增。
	    saveUriOrg(lUriDo.uriOrg, uriId, false);
	    saveUriIp(lUriDo.uriIp, uriId, false);
	    saveUriRole(lUriDo.uriRole, uriId, false);
	    saveUriRoleGroup(lUriDo.uriRoleGroup, uriId, false);
	} else { // 默认配置下，来自源码中的配置更新+新增；其它来源的配置不更新。
	    if (lUriDo.uriOrg != null)
		saveUriOrg(lUriDo.uriOrg.stream().filter(s -> s.fromTo == UriOrgEnum.FromTo.SOURCECODE).collect(Collectors.toList()), uriId, true);
	    if (lUriDo.uriIp != null && lUriDo.uriIp.fromTo == UriIpEnum.FromTo.SOURCECODE)
		saveUriIp(lUriDo.uriIp, uriId, true);
	    if (lUriDo.uriRole != null)
		saveUriRole(lUriDo.uriRole.stream().filter(s -> s.fromTo == UriRoleEnum.FromTo.SOURCECODE).collect(Collectors.toList()), uriId, true);
	    if (lUriDo.uriRoleGroup != null)
		saveUriRoleGroup(lUriDo.uriRoleGroup.stream().filter(s -> s.fromTo == UriRoleGroupEnum.FromTo.SOURCECODE).collect(Collectors.toList()), uriId, true);
	}

	// 强制更新缓存
	loadAllFromCore(true);
    }

    private int saveUri(Uri uri) {
	List<Integer> ids = jdbcTemplate.queryForList(sql2, Integer.class, uri.uri);
	if (ids.size() > 0) { // 存在则更新，不更新状态
	    jdbcTemplate.update(sql4, uri.classFullName, uri.methodName, uri.methodSignature, uri.easType.ordinal(), uri.fromTo.ordinal(), ids.get(0));
	} else { // 不存在则新建
	    jdbcTemplate.update(sql3, uri.uri, uri.classFullName, uri.methodName, uri.methodSignature, uri.easType.ordinal(), uri.fromTo.ordinal(), uri.status.ordinal());
	    ids = jdbcTemplate.queryForList(sql2, Integer.class, uri.uri);
	}
	return ids.get(0);
    }

    private void saveUriOrg(List<UriOrg> uriOrgs, Integer uriId, boolean isUpate) {
	if (uriOrgs == null || uriOrgs.isEmpty())
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql11, String.class, uriId, uriOrgs.get(0).fromTo);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    if (isUpate) {
		// TODO 待开发
		// TODO bug delete all???
//		jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺少id，不知道如何处理才好！");
	    }
	} else { // 新建
	    uriOrgs.forEach(item -> {
		jdbcTemplate.update(sql12, item.group1, item.code, uriId, item.fullCode, item.orgId, item.name, item.fullName, item.fromTo.ordinal(), item.status.ordinal());
	    });
	}
    }

    private void saveUriIp(UriIp uriIp, Integer uriId, boolean isUpate) {
	if (uriIp == null)
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql21, String.class, uriId, uriIp.fromTo);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    if (isUpate) {
		// TODO 待开发
		// TODO bug delete all???
//		jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺少id，不知道如何处理才好！");
	    }
	} else { // 新建
	    jdbcTemplate.update(sql22, uriIp.group1, uriIp.ips, uriId, uriIp.fromTo.ordinal(), uriIp.status.ordinal());
	}
    }

    private void saveUriRole(List<UriRole> uriRoles, Integer uriId, boolean isUpate) {
	if (uriRoles == null || uriRoles.isEmpty())
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql31, String.class, uriId, uriRoles.get(0).fromTo);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    if (isUpate) {
		// TODO 待开发
		// TODO bug delete all???
//		jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺少id，不知道如何处理才好！");
	    }
	} else { // 新建
	    uriRoles.forEach(item -> {
		jdbcTemplate.update(sql32, item.annotation, item.group1, item.roleId, uriId, item.fromTo.ordinal(), item.status.ordinal());
	    });
	}
    }

    private void saveUriRoleGroup(List<UriRoleGroup> uriRoleGroups, Integer uriId, boolean isUpate) {
	if (uriRoleGroups == null || uriRoleGroups.isEmpty())
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql41, String.class, uriId, uriRoleGroups.get(0).fromTo);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    if (isUpate) {
		// TODO 待开发
		// TODO bug delete all???
//		jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺少id，不知道如何处理才好！");
	    }
	} else { // 新建
	    uriRoleGroups.forEach(item -> {
		jdbcTemplate.update(sql42, item.annotation, item.group1, item.roleGroupId, uriId, item.fromTo.ordinal(), item.status.ordinal());
	    });
	}
    }
}
