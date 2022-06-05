package com.easecurity.core.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * token相关
 */
@Controller
@RequestMapping("/token")
class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 通过Refresh Token获取新的Access Token
     */
    @RequestMapping("/refreshToken")
    @ResponseBody
    public String refreshToken(String refresh_token) throws IOException {
	// TODO authorization = Bearer 7b2a9b9b9c3f480bb8641c2cf0dd74c6
	// access_token=mF_9.B5f-4.1JqM
	// or 参数access_token=mF_9.B5f-4.1JqM
	log.debug("TokenController.refreshToken.token={}", refresh_token);
	Token newToken = loginService.refreshToken(refresh_token);
	return JSON.toJSONString(newToken);
//	return JsonUtils.objectToJson(newToken);
    }
}