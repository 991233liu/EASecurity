/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.UriDo;
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
    // TODO 内存强制失效?
    private static volatile long validTime = -1;

    /**
     * 从core初始化URI信息。
     */
    @SuppressWarnings("unchecked")
    private Map<String, UriDo> loadAllFromCore() {
	// 如果Redis中有缓存，则使用Redis中；如果没有，则加载数据库并缓存到Redis
	if (redisUtil.hasKey("uri:rootList")) {
	    allUriDoMap = (Map<String, UriDo>) redisUtil.get("uri:all");
	} else {
	    Map<String, UriDo> audoAllMap = new HashMap<String, UriDo>();
	    List<Uri> all = jdbcTemplate.query("SELECT * FROM re_uri", new BeanPropertyRowMapper<>(Uri.class));
	    for (Uri uri : all) {
		// TODO 属性没加载全
		UriDo udo = new UriDo();
		udo.uri = uri;
		udo.uriOrg = jdbcTemplate.query("SELECT * FROM au_uri_org where uriid = ?", new BeanPropertyRowMapper<>(UriOrg.class), uri.id);
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
    // TODO 强制清除？
    public Map<String, UriDo> getAllUriDos() {
	// 有效期内直接返回内存缓存的数据
	if (System.currentTimeMillis() < validTime)
	    return allUriDoMap;

	return loadAllFromCore();
    }
}
