/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.framework.authentication.AbsAuthFilter;
import com.easecurity.framework.authentication.LoginService;

/**
 * 认证Filter
 *
 */
public abstract class AbsWebSecurityFilter extends AbsAuthFilter {
    private static final Logger log = LoggerFactory.getLogger(AbsWebSecurityFilter.class);

    @Autowired
    EaSecurityConfiguration eaSecurityConfiguration;
    @Autowired
    LoginService loginService;

    RSAPublicKey rsaPublicKey;

    @Override
    public EaSecurityConfiguration getConfig() {
	return eaSecurityConfiguration;
    }

    @Override
    public JWT getCurrentUserJWTFromSecurityCentre(ServletRequest request) {
	try {
	    if (rsaPublicKey == null)
		rsaPublicKey = getRSAPublicKey();
	    long s = System.currentTimeMillis();
	    JWT jwt = loginService.getCurrentUserJWT((HttpServletRequest) request, rsaPublicKey);
	    if (log.isDebugEnabled())
		System.out.println("-----## 登录消耗时间为：" + (System.currentTimeMillis() - s));
	    if (log.isDebugEnabled() && jwt != null && jwt.userDetails != null)
		System.out.println("-----## 当前登录人为：" + jwt.userDetails.account);
	    return jwt;
	} catch (IOException e) {
	    log.error("在处理JWT请求时，IO异常", e);
	} catch (JWTExpirationException e) {
	    log.error("在处理JWT请求时，SecurityCentre返回的JWT无效", e);
	} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	    log.error("在处理JWT请求时，RAS证书加载异常", e);
	}
	return null;
    }

    private RSAPublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	File file = org.springframework.util.ResourceUtils.getFile(eaSecurityConfiguration.jwt.getPublicKey());
	if (file.isFile()) {
	    return loginService.getRSAPublicKey(file);
	} else {
	    return loginService.getRSAPublicKey(eaSecurityConfiguration.jwt.getPublicKey());
	}

    }
}
