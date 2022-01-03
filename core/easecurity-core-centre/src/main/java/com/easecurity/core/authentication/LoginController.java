package com.easecurity.core.authentication;

//import com.easecurity.admin.core.re.Menu;
//import com.easecurity.core.captcha.GifCaptcha;
import com.easecurity.util.JsonUtils;
import com.easecurity.core.basis.s.GifCaptcha;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import grails.converters.JSON;
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

    @Value("${loginCaptcha.disable:false}")
    boolean disable;
    @Value("${loginCaptcha.gifCaptcha.length:5}")
    Integer gifCaptchaLength;
    @Value("${loginCaptcha.gifCaptcha.delay:100}")
    Integer gifCaptchaDelay;
    @Value("${loginCaptcha.gifCaptcha.validTime:300000}")
    Integer validTime;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
	ModelAndView mav = new ModelAndView();
	if (error != null) {
	    mav.addObject("error", "用户名或者密码不正确");
	}
	if (logout != null) {
	    mav.addObject("msg", "退出成功");
	}
	mav.setViewName("/auth/login.html");
	return mav;
    }

//    def auth() {

//        ConfigObject conf = getConf();
//
//        if (springSecurityService.isLoggedIn()) {
//            redirect uri: conf.successHandler.defaultTargetUrl;
//            return
//        }
//
//        Map<String, Object> map = disable ? null : getGifCaptcha()
//        String postUrl = request.contextPath + conf.apf.filterProcessesUrl
//        render view: 'auth', model: [postUrl            : postUrl,
//                                     rememberMeParameter: conf.rememberMe.parameter,
//                                     usernameParameter  : conf.apf.usernameParameter,
//                                     passwordParameter  : conf.apf.passwordParameter,
//                                     gspLayout          : conf.gsp.layoutAuth,
//                                     gifCaptcha         : map]
//    }

//    def allMenu() {
//        Map menuTree = getSession().getAttribute('allMenuTree')
//        if (!menuTree) {
//            Menu menu = Menu.findByCode('adminRoot')
//            menuTree = getMenuTree(menu)
//            getSession().setAttribute('allMenuTree', menuTree)
//        }
//        respond menuTree.get('children') ?: [], formats: ['json']
//    }
//
    @GetMapping("/gifCaptcha")
    @ResponseBody
    public String gifCaptcha(HttpSession session) {
	Map<String, Object> map = disable ? new HashMap<>() : getGifCaptcha(session);
//        respond map, formats: ['json']
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

//    private Map getMenuTree(Menu menu) {
//        Map map = new HashMap();
//        map.putAll(menu.properties);
//        map.put('id', menu.id);
//        map.remove('parent');
//        List children = [];
//        (menu.children as List).sort { x, y ->
//            x.sortNumber.compareTo(y.sortNumber);
//        }.each {
//            children.add(getMenuTree(it));
//        }
//        if (!children.isEmpty()) map.put('children', children);
//        else map.remove('children');
//        return map;
//    }
}