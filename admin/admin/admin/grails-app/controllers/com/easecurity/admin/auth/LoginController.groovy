package com.easecurity.admin.auth

import com.easecurity.admin.utils.ServletUtils
import com.easecurity.core.captcha.GifCaptcha
import com.easecurity.admin.auth.GifCaptcha as DGifCaptcha
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.userdetails.UserDetails

import javax.servlet.http.HttpServletResponse

@Secured('permitAll')
class LoginController extends grails.plugin.springsecurity.LoginController {

    @Value('${loginCaptcha.disable:false}')
    boolean disable
    @Value('${loginCaptcha.gifCaptcha.length:5}')
    Integer gifCaptchaLength
    @Value('${loginCaptcha.gifCaptcha.delay:100}')
    Integer gifCaptchaDelay
    @Value('${loginCaptcha.gifCaptcha.validTime:300000}')
    Integer validTime

    def auth() {
        println("----# b1")

        def conf = getConf()

        if (springSecurityService.isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
            return
        }

        response.sendError HttpServletResponse.SC_FORBIDDEN // 403
    }

    /* 当前登录人信息 */
    def userInfo() {
        Map<String, Object> map = [username: 'anonymous']
        UserDetails principal = ServletUtils.currentUser
        if (principal instanceof CustomUserDetails) {  // 登录用户
//            CustomUserDetails principal
            map = [username     : principal.username, fullName: principal.fullName, avatar: principal.avatar, email: principal.email,
                   lastLoginTime: principal.lastLoginTime, roles: principal.authorities*.authority]
            respond map, model: []
        } else {  // 匿名访问
            respond map, model: []
        }
    }

    /* 获取验证码 */
    def gifCaptcha() {
        Map<String, Object> map = disable ? null : getGifCaptcha()
        respond map, model: []
    }

    private Map getGifCaptcha() {
        GifCaptcha gifCaptcha = new GifCaptcha(130, 48, gifCaptchaLength, gifCaptchaDelay)
        String key = UUID.randomUUID().toString()
        String verCode = gifCaptcha.text().toLowerCase()
        DGifCaptcha dDifCaptcha1 = new DGifCaptcha()
        // TODO
//        dDifCaptcha1.sessionId = getSession().getId()
        dDifCaptcha1.gkey = key
        dDifCaptcha1.gvalue = verCode
        dDifCaptcha1.validTime = System.currentTimeMillis() + validTime
        com.easecurity.admin.auth.GifCaptcha.withTransaction {
            dDifCaptcha1.save(flush: true)
        }
        if (log.isDebugEnabled()) log.debug("----# 图片验证码为：" + verCode + "==" + key)
        // TODO 数据库验证
        // TODO Redis验证
        getSession().setAttribute("GifCaptcha", dDifCaptcha1)

        Map<String, Object> map = new HashMap<>()
        map.put("key", key);
        map.put("image", gifCaptcha.toBase64());
        return map;
    }
}
