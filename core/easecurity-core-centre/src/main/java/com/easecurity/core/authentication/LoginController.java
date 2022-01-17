package com.easecurity.core.authentication;

import com.easecurity.util.JsonUtils;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.s.GifCaptcha;
import com.easecurity.core.utils.ServletUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    JwtEncoder encoder;
    @Autowired
    JwtDecoder decoder;

    @Value("${loginCaptcha.disable:true}")
    boolean disable;
    @Value("${loginCaptcha.gifCaptcha.length:5}")
    Integer gifCaptchaLength;
    @Value("${loginCaptcha.gifCaptcha.delay:100}")
    Integer gifCaptchaDelay;
    @Value("${loginCaptcha.gifCaptcha.validTime:300000}")
    Integer validTime;
    @Value("${easecurity.jwt.validTime:300}")
    Integer JWTValidTime;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("/auth/login.html");
	if (error != null) {
//	    mav.addObject("error", "用户名或者密码不正确");
	    mav.setViewName("/auth/loginError.html");
	}
	if (logout != null) {
//	    mav.addObject("msg", "退出成功");
	    mav.setViewName("/auth/logout.html");
	}
	return mav;
    }

    @GetMapping("/currentUserJWT")
    @ResponseBody
    // TODO 后台访问？？？绑定IP
    public String currentUserJWT(HttpServletResponse response, Authentication authentication) throws IOException {
	UserDetails user = ServletUtils.getCurrentUserDetails();
	if (user == null) { // 未登录时
	    response.setStatus(203);
	    return "anonymousUser";
	}
	Instant now = Instant.now();
	String scope = JSON.toJSONString(user);
	JwtClaimsSet claims = JwtClaimsSet.builder()
			.issuer("SecurityCentre")
			.issuedAt(now)
			.expiresAt(now.plusSeconds(JWTValidTime))
			.subject(authentication.getName())
			.claim("userDetails", scope)
			.build();
	return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    @GetMapping("/currentUserJWT2")
    @ResponseBody
    // TODO 测试用代码，需要删掉
    public String currentUserJWT2(HttpServletResponse response, Authentication authentication) throws IOException {
	Jwt jwt = decoder.decode(this.currentUserJWT(response, authentication));
	return JSON.toJSONString(jwt.getClaims());
    }

    @GetMapping("/gifCaptcha")
    @ResponseBody
    public String gifCaptcha(HttpSession session) {
	Map<String, Object> map = disable ? new HashMap<>() : getGifCaptcha(session);
	return JsonUtils.objectToJson(map);
    }

    private Map<String, Object> getGifCaptcha(HttpSession session) {
	com.easecurity.core.captcha.GifCaptcha gifCaptcha = new com.easecurity.core.captcha.GifCaptcha(130, 48, gifCaptchaLength, gifCaptchaDelay);
	String key = UUID.randomUUID().toString();
	String verCode = gifCaptcha.text().toLowerCase();
	GifCaptcha dDifCaptcha1 = new GifCaptcha();
	dDifCaptcha1.sessionId = session.getId();
	dDifCaptcha1.key2 = key;
	dDifCaptcha1.value = verCode;
	dDifCaptcha1.validTime = System.currentTimeMillis() + validTime;
//        DGifCaptcha.withTransaction {
//            dDifCaptcha1.save(flush: true);
//        }
	if (log.isDebugEnabled())
	    log.debug("----# 图片验证码为：" + verCode);
	// TODO 数据库验证
	// TODO Redis验证
	session.setAttribute("GifCaptcha", dDifCaptcha1);

	Map<String, Object> map = new HashMap<>();
	map.put("key", key);
	map.put("image", gifCaptcha.toBase64());
	return map;
    }
}