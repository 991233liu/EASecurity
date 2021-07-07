/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EasType;
import com.easecurity.core.basis.UriDo;
import com.easecurity.core.basis.UserDo;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.re.Uri;
import com.easecurity.framework.access.AccessRegister;

//import com.easecurity.core.redis.RedisUtil;

/**
 * 接口（访问）访问控制服务
 *
 */
// TODO 转成纯java的
@Service
public class UriAccessService {
    private static final Logger log = LoggerFactory.getLogger(UriAccessService.class);

//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @Autowired
//    RedisUtil redisUtil;

    AccessRegister accessRegister = AccessRegister.getInstance("http://127.0.0.1/SecurityCentre");

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
	Uri u = new Uri();
	u.id = 1;
	u.uri = uri;
	u.classFullName = classFullName;
	u.methodName = methodName;
	u.methodSignature = methodSignature;
	u.easType = eas.type();
	u.fromTo = "2";
	UriOrg uo = new UriOrg();
	uo.uriid = 1;
	uo.orgid = 1;
	uo.status = "0";
	uo.fromTo = "2";
	UriOrg uo2 = new UriOrg();
	uo2.uriid = 1;
	uo2.orgid = 4;
	uo2.status = "0";
	uo2.fromTo = "2";
	UriDo uriDo = new UriDo();
	uriDo.uri = u;
	uriDo.uriOrg = new ArrayList<>();
	uriDo.uriOrg.add(uo);
	uriDo.uriOrg.add(uo2);
	uriDos.put(uriDo.uri.uri, uriDo);
	// TODO 每次都取一次吗？？？
	uriDos = accessRegister.getAllUriDos();
    }

    /**
     * 校验访问权限
     */
    // TODO 多种模式
    public boolean validation(EaSecured eas, String uri, UserDo userDo) {
	boolean[] flag = { false };
	EasType type = eas.type();
	if (type == EasType.SOURCE_ONLY) { // 源码模式
	    String org = eas.org();

	} else { // 其它模式
	    if (userDo != null) {
		UriDo uriDo = uriDos.get(uri);
		if (uriDo != null) {
		    userDo.orgUsers.forEach(ou -> {
			if (uriDo.havePermissionThroughOrgById(ou.orgid)) {
			    flag[0] = true;
			    return;
			}
		    });
		} else {
		    log.error("---## 发现了一个不存在数据库的URI，请联系管理员，URI为：{}",uri);
		}
	    }
	}
	return flag[0];
    }

    private static volatile Map<String, UriDo> uriDos = new HashMap<>();
    private static volatile long lastModifyTime = -1;
}
