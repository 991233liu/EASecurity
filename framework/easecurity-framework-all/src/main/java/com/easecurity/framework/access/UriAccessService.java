/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.basis.UserDo;

//import com.easecurity.core.redis.RedisUtil;

/**
 * 接口（访问）访问控制服务
 *
 */
// TODO 转成纯java的
@Service
public class UriAccessService {

//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @Autowired
//    RedisUtil redisUtil;
    @Value("${easecurity.server.url:}")
    private String easCentreUrl = null;

    /**
     * 更新URI的授权信息。如果URI已经存在，则更新状态以外其它信息；如果不存在则新建
     * 
     * @param eas
     * @param uri
     * @param classFullName
     * @param methodName
     * @param methodSignature
     */
    public void saveUriPermissions(EaSecured eas, String uri, String classFullName, String methodName, String methodSignature) {
	UriService uriService = AccessServiceFactory.getUriService(easCentreUrl);
	uriService.saveUriPermissions(eas, uri, classFullName, methodName, methodSignature);
    }

    /**
     * 校验访问权限
     */
    public boolean validation(EaSecured eas, String uri, UserDo userDo) {
	UriService uriService = AccessServiceFactory.getUriService(easCentreUrl);
	return uriService.validation(eas, uri, userDo);
    }
}
