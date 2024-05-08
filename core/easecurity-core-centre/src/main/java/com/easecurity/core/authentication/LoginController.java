package com.easecurity.core.authentication;

import com.easecurity.util.JsonUtils;

import com.easecurity.core.basis.s.GifCaptcha;
import com.easecurity.core.utils.CacheUtil;
import com.easecurity.core.utils.MessageSourceUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private MessageSourceUtil messageSourceUtil;

    @Value("${loginCaptcha.disable:true}")
    boolean disable;
    @Value("${loginCaptcha.gifCaptcha.length:5}")
    private Integer gifCaptchaLength;
    @Value("${loginCaptcha.gifCaptcha.delay:100}")
    private Integer gifCaptchaDelay;
    @Value("${loginCaptcha.gifCaptcha.validTime:300000}")
    private Integer validTime;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "errorGifCaptcha", required = false) String errorGifCaptcha,
            @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String redirect_url = request.getParameter("redirect_url");
        String faile_url = request.getParameter("faile_url");
        mav.setViewName("/auth/login.html");
        if (error != null) {
            mav.addObject("message", messageSourceUtil.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else if (errorGifCaptcha != null) {
            mav.addObject("message", messageSourceUtil.getMessage("AbstractUserDetailsAuthenticationProvider.badGifCaptcha", "Bad gifCaptcha"));
        } else if (logout != null) {
            mav.addObject("message", messageSourceUtil.getMessage("LoginController.logout", "Succeed logout"));
        } else if ((redirect_url != null && !"".equals(redirect_url)) || ((faile_url != null && !"".equals(faile_url)))) {
            mav.setViewName("/auth/login_ajax.html");
        }
        return mav;
    }

    @GetMapping("/gifCaptcha")
    @ResponseBody
    public String gifCaptcha() {
        Map<String, Object> map = disable ? new HashMap<>() : getGifCaptcha();
        return JsonUtils.objectToJson(map);
    }

    private Map<String, Object> getGifCaptcha() {
        com.easecurity.core.captcha.GifCaptcha gifCaptcha = new com.easecurity.core.captcha.GifCaptcha(130, 48, gifCaptchaLength, gifCaptchaDelay);
        String key = UUID.randomUUID().toString();
        String verCode = gifCaptcha.text().toLowerCase();
        if (log.isDebugEnabled())
            log.debug("----# 图片验证码为：" + verCode);
        GifCaptcha dDifCaptcha1 = new GifCaptcha();
        dDifCaptcha1.gkey = key;
        dDifCaptcha1.gvalue = verCode;
        dDifCaptcha1.validTime = System.currentTimeMillis() + validTime;
        // TODO 数据库验证
//        DGifCaptcha.withTransaction {
//            dDifCaptcha1.save(flush: true);
//        }
        CacheUtil.setCache("GifCaptcha:" + key, dDifCaptcha1, dDifCaptcha1.validTime);

        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("image", gifCaptcha.toBase64());
        return map;
    }
}