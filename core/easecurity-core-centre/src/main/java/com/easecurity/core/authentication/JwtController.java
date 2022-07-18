package com.easecurity.core.authentication;

import com.easecurity.core.access.annotation.EaSecuredIP;
import com.easecurity.core.basis.s.UserToken;
import com.easecurity.core.utils.CacheUtil;
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

    /**
     * 基于session的：获取当前登录人的jwt信息
     */
    @GetMapping("/currentUserJWT")
    @ResponseBody
    @EaSecuredIP
    public String currentUserJWT(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDetails user = ServletUtils.getCurrentUser();
        if (user == null) { // 未登录时
            response.setStatus(HttpStatus.FORBIDDEN.value());
            log.info("这里发现一个未授权访问");
            return "anonymousUser";
        }
        String jwt = (String) CacheUtil.getSessionCache("JWT.str");
        // 优先从session中取，还在有效期内的直接返回，不用重新生成
        if (jwt != null && !"".equals(jwt)) {
            Long expiresAt = (Long) CacheUtil.getSessionCache("JWT.expiresAt");
            if (expiresAt != null && Instant.now().getEpochSecond() < expiresAt)
                return jwt;
        }
        JwtClaimsSet claims = loginService.createJwt(user);
        jwt = loginService.getJwtTokenValue(claims);
        CacheUtil.setAccessTokenCache("JWT.jti", claims.getClaim("jti"));
        CacheUtil.setAccessTokenCache("JWT.str", jwt);
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
        String jwt = (String) CacheUtil.getAccessTokenCache("JWT:" + at + ":str");
        // 优先从缓存中取，还在有效期内的直接返回，不用重新生成
        if (jwt != null && !"".equals(jwt)) {
            Long expiresAt = Long.valueOf((String) CacheUtil.getAccessTokenCache("JWT:expiresAt"));
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
            CacheUtil.setAccessTokenCache("JWT:str", userToken.jwt);
            CacheUtil.setAccessTokenCache("JWT:expiresAt", String.valueOf(userToken.accessTokenExpires.getEpochSecond()));
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
    }
}