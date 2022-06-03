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
    public String refreshToken(String token) throws IOException {
	log.debug("TokenController.refreshToken.token={}", token);
	Token newToken = loginService.refreshToken(token);
	return JSON.toJSONString(newToken);
//	return JsonUtils.objectToJson(newToken);
    }
}