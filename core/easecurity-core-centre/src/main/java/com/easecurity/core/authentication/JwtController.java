package com.easecurity.core.authentication;

import com.easecurity.core.access.annotation.EaSecuredIP;
import com.easecurity.core.utils.ServletUtils;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token")
class JwtController {
    private static final Logger log = LoggerFactory.getLogger(JwtController.class);

    @GetMapping("/currentUserJWT")
    @ResponseBody
    @EaSecuredIP
    public String currentUserJWT(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
	UserDetails user = ServletUtils.getCurrentUserDetails();
	if (user == null) { // 未登录时
	    response.setStatus(203);
	    log.info("这里发现一个未授权访问");
	    return "anonymousUser";
	}
	String jwt = (String) request.getSession().getAttribute("JWT.str");
	Long expiresAt = (Long) request.getSession().getAttribute("JWT.expiresAt");
	// 优先从session中取，还在有效期内的直接返回，不用重新生成
	if (jwt != null && !"".equals(jwt)) {
	    if (expiresAt != null && Instant.now().getEpochSecond() < expiresAt)
		return jwt;
	}
	return jwt;
    }
}