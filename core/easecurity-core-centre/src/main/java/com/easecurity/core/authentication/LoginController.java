package com.easecurity.core.authentication;

//import com.easecurity.admin.core.re.Menu;
//import com.easecurity.core.captcha.GifCaptcha;
import com.easecurity.util.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.easecurity.core.basis.UserService;
import com.easecurity.core.basis.s.GifCaptcha;
import com.easecurity.core.utils.ServletUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    @Value("${loginCaptcha.disable:true}")
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

    @GetMapping("/currentUser")
    @ResponseBody
    // TODO 加密？？
    // TODO 后台访问？？？
    public String currentUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
	CustomUserDetails user = ServletUtils.getCurrentUser();
	if (user == null || user.isAnonymousUser()) { // TODO 未登录时？
	    response.setStatus(203);
	    return "anonymousUser";
	} else { // 登录用户
	    UserDetails userDetails = new UserDetails();
	    userDetails.id=user.id;
	    userDetails.account=user.getUsername();
	    userDetails.name=user.fullName;
	    userDetails.icon=user.icon;
	    userDetails.identities=user.identities;
	    return JSON.toJSONString(userDetails);
//	    UserDo userdo = (UserDo) session.getAttribute("userdo");
//	    if (userdo == null) {
//		userdo = userService.getUserDoByAccount(user.getUsername());
//		session.setAttribute("userdo", userdo);
//	    }
//	    // 清空密码，不能传递
//	    userdo.user.pd = null;
//	    return JSON.toJSONString(userdo);
//	    return JsonUtils.objectToJson(userdo);
	}
    }

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