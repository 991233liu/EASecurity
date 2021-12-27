package com.easecurity.admin.auth

import com.easecurity.core.captcha.GifCaptcha
import grails.config.Config

class LoginController extends grails.plugin.springsecurity.LoginController {

//    List<String> coordinatePositions

    def auth() {

        ConfigObject conf = getConf()

        if (springSecurityService.isLoggedIn()) {
            redirect uri: conf.successHandler.defaultTargetUrl
            return
        }

//        String postUrl = request.contextPath + conf.apf.filterProcessesUrl
//        render view: 'auth', model: [postUrl: postUrl,
//                                     rememberMeParameter: conf.rememberMe.parameter,
//                                     usernameParameter: conf.apf.usernameParameter,
//                                     passwordParameter: conf.apf.passwordParameter,
//                                     gspLayout: conf.gsp.layoutAuth]

//        Collections.shuffle(coordinatePositions)
//        String position = coordinatePositions.first()

        String postUrl = request.contextPath + conf.apf.filterProcessesUrl
        render view: 'auth', model: [postUrl            : postUrl,
                                     rememberMeParameter: conf.rememberMe.parameter,
                                     usernameParameter  : conf.apf.usernameParameter,
                                     passwordParameter  : conf.apf.passwordParameter,
                                     gspLayout          : conf.gsp.layoutAuth,
                                     pictureCode          : getPictureCode()]
//                                     position           : position]
    }

//    @Override
//    void setConfiguration(Config co) {
//        coordinatePositions = co.getProperty('security.coordinate.positions', List, []) as List<String>
//    }

    private Map getPictureCode() {
        GifCaptcha gifCaptcha = new GifCaptcha(130, 48, 5);
        Map<String, Object> map = new HashMap<>();
        String key = UUID.randomUUID().toString();
        String verCode = gifCaptcha.text().toLowerCase();
        map.put("key", key);
        map.put("image", gifCaptcha.toBase64());
        System.out.println(verCode);

//        redisTemplate.opsForValue().set(key, verCode, 5, TimeUnit.MINUTES);
        return map;
    }
}