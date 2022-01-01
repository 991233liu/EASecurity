package com.easecurity.admin.auth

import com.easecurity.admin.core.re.Menu
import com.easecurity.core.captcha.GifCaptcha
import com.easecurity.admin.auth.GifCaptcha as DGifCaptcha
import grails.converters.JSON
import org.springframework.beans.factory.annotation.Value

class LoginController extends grails.plugin.springsecurity.LoginController {

    @Value('${loginCaptcha.disable:true}')
    boolean disable
    @Value('${loginCaptcha.gifCaptcha.length:5}')
    Integer gifCaptchaLength
    @Value('${loginCaptcha.gifCaptcha.delay:100}')
    Integer gifCaptchaDelay

    def auth() {

        ConfigObject conf = getConf()

        if (springSecurityService.isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
            return
        }

        Map<String, Object> map = disable ? null : getGifCaptcha()
        String postUrl = request.contextPath + conf.apf.filterProcessesUrl
        render view: 'auth', model: [postUrl            : postUrl,
                                     rememberMeParameter: conf.rememberMe.parameter,
                                     usernameParameter  : conf.apf.usernameParameter,
                                     passwordParameter  : conf.apf.passwordParameter,
                                     gspLayout          : conf.gsp.layoutAuth,
                                     gifCaptcha         : map]
    }

    def allMenu() {
        Map menuTree = getSession().getAttribute('allMenuTree')
        if (!menuTree) {
            Menu menu = Menu.findByCode('adminRoot')
            menuTree = getMenuTree(menu)
            getSession().setAttribute('allMenuTree', menuTree)
        }
        respond menuTree.get('children') ?: [], formats: ['json']
    }

    def gifCaptcha() {
        Map<String, Object> map = disable ? null : getGifCaptcha()
        respond map, formats: ['json']
    }

    private Map getGifCaptcha() {
        GifCaptcha gifCaptcha = new GifCaptcha(130, 48, gifCaptchaLength, gifCaptchaDelay)
        String key = UUID.randomUUID().toString()
        String verCode = gifCaptcha.text().toLowerCase()
        DGifCaptcha dDifCaptcha1 = new DGifCaptcha()
        dDifCaptcha1.sessionId = getSession().getId()
        dDifCaptcha1.key2 = key
        dDifCaptcha1.value = verCode
        // TODO 改到配置文件中
        dDifCaptcha1.validTime = System.currentTimeMillis() + 300000
        DGifCaptcha.withTransaction {
            dDifCaptcha1.save(flush: true)
        }
        if (log.isDebugEnabled()) log.debug("----# 图片验证码为：" + verCode)
        // TODO 数据库验证
        // TODO Redis验证
        getSession().setAttribute("GifCaptcha", dDifCaptcha1)

        Map<String, Object> map = new HashMap<>()
        map.put("key", key);
        map.put("image", gifCaptcha.toBase64());
        return map;
    }

    private Map getMenuTree(Menu menu) {
        Map map = new HashMap()
        map.putAll(menu.properties)
        map.put('id', menu.id)
        map.remove('parent')
        List children = []
        (menu.children as List).sort { x, y ->
            x.sortNumber.compareTo(y.sortNumber)
        }.each {
            children.add(getMenuTree(it))
        }
        if (!children.isEmpty()) map.put('children', children)
        else map.remove('children')
        map
    }
}