package com.easecurity.core.authentication;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.access.annotation.EaSecuredIP;
import com.easecurity.core.utils.ServletUtils;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jwt")
class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private JwtEncoder encoder;
    @Autowired
    private JwtDecoder decoder;
    @Autowired
    private LoginService loginService;

    @Value("${loginCaptcha.disable:true}")
    boolean disable;
    @Value("${loginCaptcha.gifCaptcha.length:5}")
    private Integer gifCaptchaLength;
    @Value("${loginCaptcha.gifCaptcha.delay:100}")
    private Integer gifCaptchaDelay;
    @Value("${loginCaptcha.gifCaptcha.validTime:300000}")
    private Integer validTime;

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
	JwtClaimsSet claims = loginService.createJWT(user, authentication);
	request.getSession().setAttribute("JWT.jti", claims.getClaim("jti"));
	request.getSession().setAttribute("JWT.str", encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
	request.getSession().setAttribute("JWT.expiresAt", claims.getExpiresAt().getEpochSecond());
	return jwt;
    }

    @GetMapping("/getJWT")
    @ResponseBody
    @EaSecuredIP
    public String getJWT(String at) throws IOException {
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

    @GetMapping("/currentUserJWT2")
    @ResponseBody
    // TODO 测试用代码，需要删掉
    public String currentUserJWT2(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
	Jwt jwt = decoder.decode(this.currentUserJWT(request, response, authentication));
	return JSON.toJSONString(jwt.getClaims());
    }
}