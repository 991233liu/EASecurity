package com.easecurity.core.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.utils.ServletUtils;

/**
 * token相关
 */
@Controller
@RequestMapping("/token")
class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private LogoutService logoutService;

    /**
     * 通过Refresh Token获取新的Access Token
     */
    @RequestMapping("/refreshToken")
    @ResponseBody
    public String refreshToken(String refresh_token) throws IOException {
	log.debug("TokenController.refreshToken.token={}", refresh_token);
	Token newToken = loginService.refreshToken(refresh_token);
	String at = ServletUtils.getAccessToken();
	if (at != null)
	    logoutService.asynLogout(null, at, at);
	return JSON.toJSONString(newToken);
//	return JsonUtils.objectToJson(newToken);
    }
}