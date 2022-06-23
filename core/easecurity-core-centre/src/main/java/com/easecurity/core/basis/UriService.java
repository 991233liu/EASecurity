/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.au.UriIp;
import com.easecurity.core.basis.au.UriOrg;
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

    String sql = "INSERT INTO re_uri (uri, class_full_name, method_name, method_signature, eas_type, from_to, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String sql2 = "UPDATE re_uri set class_full_name=?, method_name=?, method_signature=?, eas_type=?, from_to=? where id=?";
    String sql3 = "INSERT INTO au_uri_org (group1, code, uri_id, full_code, org_id, name, full_name, from_to, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    String sql4 = "UPDATE au_uri_org set from_to=? where id=?";
    String sql5 = "SELECT * FROM re_uri";
    String sql6 = "SELECT id FROM re_uri where uri = ?";
    String sql7 = "SELECT * FROM au_uri_org where uri_id = ?";
    String sql8 = "SELECT id FROM au_uri_org where uri_id = ?";
    String sql9 = "SELECT * FROM au_uri_ip where uri_id = ?";
    String sql10 = "SELECT id FROM au_uri_ip where uri_id = ?";
    String sql11 = "INSERT INTO au_uri_ip(group1, ips, uri_id, status, from_to) VALUES (?, ?, ?, ?, ?)";

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
	    List<Uri> all = jdbcTemplate.query(sql5, new BeanPropertyRowMapper<>(Uri.class));
	    for (Uri uri : all) {
		// TODO 属性没加载全
		UriDo udo = new UriDo();
		udo.uri = uri;
		udo.uriOrg = jdbcTemplate.query(sql7, new BeanPropertyRowMapper<>(UriOrg.class), uri.id);
		List<UriIp> uriIp = jdbcTemplate.query(sql9, new BeanPropertyRowMapper<>(UriIp.class), uri.id);
		if (!uriIp.isEmpty())
		    udo.uriIp = uriIp.get(0);
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
	List<Integer> ids = jdbcTemplate.queryForList(sql6, Integer.class, lUriDo.uri.uri);
	if (ids.size() > 0) { // 存在则更新，不更新状态
	    jdbcTemplate.update(sql2, lUriDo.uri.classFullName, lUriDo.uri.methodName, lUriDo.uri.methodSignature, lUriDo.uri.easType.ordinal(), lUriDo.uri.fromTo.ordinal(),
		    ids.get(0));
	} else { // 不存在则新建
	    jdbcTemplate.update(sql, lUriDo.uri.uri, lUriDo.uri.classFullName, lUriDo.uri.methodName, lUriDo.uri.methodSignature, lUriDo.uri.easType.ordinal(),
		    lUriDo.uri.fromTo.ordinal(), lUriDo.uri.status.ordinal());
	    ids = jdbcTemplate.queryForList(sql6, Integer.class, lUriDo.uri.uri);
	}
	saveUriOrg(lUriDo.uriOrg, ids.get(0), lUriDo.uri.easType);
	saveUriIp(lUriDo.uriIp, ids.get(0), lUriDo.uri.easType);
	// 强制更新缓存
	loadAllFromCore(true);
    }

    private void saveUriOrg(List<UriOrg> uriOrgs, Integer uriId, EasType easType) {
	if (uriOrgs == null || uriOrgs.isEmpty())
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql8, String.class, uriId);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    // 源码模式才更新，数据库模式下以数据库为准不能更新
	    if (easType == EasType.SOURCE_ONLY) {
		// TODO bug delete all???
//		jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺乏业务场景，不知道如何处理才好！");
	    }
	} else { // 新建
	    uriOrgs.forEach(item -> {
		jdbcTemplate.update(sql3, item.group1, item.code, uriId, item.fullCode, item.orgId, item.name, item.fullName, item.fromTo.ordinal(), item.status.ordinal());
	    });
	}
    }

    private void saveUriIp(UriIp uriIp, Integer uriId, EasType easType) {
	if (uriIp == null)
	    return;
	List<String> ids = jdbcTemplate.queryForList(sql10, String.class, uriId);
	if (ids.size() > 0) { // 更新。更新时不更新状态
	    // 源码模式才更新，数据库模式下以数据库为准不能更新
	    if (easType == EasType.SOURCE_ONLY) {
		// TODO bug delete all???
//			jdbcTemplate.update(sql4, item.fromTo.ordinal(), ids2.get(0));
		log.error("更新功能还未开发，主要是因为缺乏业务场景，不知道如何处理才好！");
	    }
	} else { // 新建
	    jdbcTemplate.update(sql11, uriIp.group1, uriIp.ips, uriId, uriIp.fromTo.ordinal(), uriIp.status.ordinal());
	}
    }
}
