package com.easecurity.core.authentication;

import com.easecurity.core.access.annotation.EaSecuredIP;
import com.easecurity.core.basis.s.UserToken;
import com.easecurity.core.redis.RedisUtil;
import com.easecurity.core.utils.ServletUtils;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * jwt相关（不对外网开发）
 */
@RestController
@RequestMapping("/jwt")
class JwtController {
    private static final Logger log = LoggerFactory.getLogger(JwtController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 基于session的：获取当前登录人的jwt信息
     */
    @GetMapping("/currentUserJWT")
    @ResponseBody
    @EaSecuredIP
    public String currentUserJWT(HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDetails user = ServletUtils.getCurrentUserDetails();
	if (user == null) { // 未登录时
	    response.setStatus(HttpStatus.FORBIDDEN.value());
	    log.info("这里发现一个未授权访问");
	    return "anonymousUser";
	}
	String jwt = (String) request.getSession().getAttribute("JWT.str");
	// 优先从session中取，还在有效期内的直接返回，不用重新生成
	if (jwt != null && !"".equals(jwt)) {
	    Long expiresAt = (Long) request.getSession().getAttribute("JWT.expiresAt");
	    if (expiresAt != null && Instant.now().getEpochSecond() < expiresAt)
		return jwt;
	}
	JwtClaimsSet claims = loginService.createJwt(user);
	jwt = loginService.getJwtTokenValue(claims);
	request.getSession().setAttribute("JWT.jti", claims.getClaim("jti"));
	request.getSession().setAttribute("JWT.str", jwt);
	request.getSession().setAttribute("JWT.expiresAt", claims.getExpiresAt().getEpochSecond());
	return jwt;
    }

    /**
     * 基于OpaqueAccessToken的JWT：获取OpaqueAccessToken对应的jwt信息。
     * 若jwt存在：直接返回；不存在且未过期，创建并返回。
     */
    @GetMapping("/getJWT")
    @ResponseBody
    @EaSecuredIP
    public String getJWT(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String at = ServletUtils.getAccessToken();
	if (at == null) {
	    response.setStatus(HttpStatus.FORBIDDEN.value());
	    log.info("这里发现一个未授权访问");
	    return "anonymousUser";
	}
	String jwt = (String) redisUtil.get("JWT:" + at + ":str");
	// 优先从缓存中取，还在有效期内的直接返回，不用重新生成
	if (jwt != null && !"".equals(jwt)) {
	    Long expiresAt = Long.valueOf((String) redisUtil.get("JWT:" + at + ":expiresAt"));
	    if (expiresAt != null && Instant.now().getEpochSecond() < expiresAt)
		return jwt;
	}
	// 缓存中不存在，则新建
	UserToken userToken = loginService.getValidUserToken(at);
	if (userToken == null) { // 未登录时或者已经过期
	    response.setStatus(HttpStatus.UNAUTHORIZED.value());
	    log.info("这里发现一个过期的访问");
	    return "expired";
	} else {
	    long timeOut = userToken.accessTokenExpires.getEpochSecond() - Instant.now().getEpochSecond();
	    redisUtil.set("JWT:" + at + ":str", userToken.jwt, timeOut);
	    redisUtil.set("JWT:" + at + ":expiresAt", String.valueOf(userToken.accessTokenExpires.getEpochSecond()), timeOut);
	    return userToken.jwt;
	}
    }

    /**
     * 基于OpaqueAccessToken的JWT：刷新OpaqueAccessToken对应的jwt信息
     */
    @GetMapping("/refreshJWT")
    @ResponseBody
    @EaSecuredIP
    public String refreshJWT(HttpServletRequest request) throws IOException {
	return null;
//	UserDetails user = ServletUtils.getCurrentUserDetails();
//	if (user == null) { // 未登录时
//	    response.setStatus(203);
//	    return "anonymousUser";
//	}
//	String jwt = (String) request.getSession().getAttribute("JWT.str");
//	Long expiresAt = (Long) request.getSession().getAttribute("JWT.expiresAt");
//	// 优先从session中取，还在有效期内的直接返回，不用重新生成
//	if (jwt != null && !"".equals(jwt)) {
//	    if (expiresAt != null && Instant.now().getEpochSecond() < expiresAt)
//		return jwt;
//	}
//	JwtClaimsSet claims = loginService.createJWT(user, authentication);
//	request.getSession().setAttribute("JWT.jti", claims.getClaim("jti"));
//	request.getSession().setAttribute("JWT.str", encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
//	request.getSession().setAttribute("JWT.expiresAt", claims.getExpiresAt().getEpochSecond());
//	return jwt;
    }
}