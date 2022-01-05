package com.easecurity.admin.auth

import com.easecurity.admin.core.b.User
import com.easecurity.admin.utils.ServletUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import static org.springframework.http.HttpStatus.NO_CONTENT

class ChangePasswordController {

    @Value('${loginCaptcha.disable:true}')
    boolean disable
    @Value('${loginCaptcha.gifCaptcha.length:5}')
    Integer gifCaptchaLength
    @Value('${loginCaptcha.gifCaptcha.delay:100}')
    Integer gifCaptchaDelay
    @Value('${loginCaptcha.gifCaptcha.validTime:300000}')
    Integer validTime

    def index() {
        render view: '/login/changePassword', model: [gifCaptcha: getGifCaptcha()]
    }

    def changeOwnerPassword() {
        System.out.println(params)
        GifCaptcha gifCaptcha1 = (GifCaptcha) getSession()?.getAttribute("GifCaptcha")
        System.out.println("-------# 本地的验证码value为：" + gifCaptcha1?.value + disable)
        // TODO 数据库验证
        // TODO Redis验证
        if (!disable && !(gifCaptcha1.validTime > System.currentTimeMillis() && gifCaptcha1.value.equals(params.gifCaptchaValue.toLowerCase()))) {
            log.debug("Authentication failed: gifCaptcha note valid");
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'AbstractUserDetailsAuthenticationProvider.badCredentials', default: "Bad GifCaptcha")
                    redirect action: "index", method: "GET"
                }
                '*' { render status: NO_CONTENT }
            }
            return
        }

        // 校验密码
        CustomUserDetails userDetails = ServletUtils.getCurrentUser()
        System.out.println("-------# 1=" + userDetails.username)
        System.out.println("-------# 1=" + userDetails.properties)
//        if (authentication.getCredentials() == null) {
//            loginFail(customUserDetails)
//            log.debug("Authentication failed: no credentials provided1")
//            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"))
//        }

        String oldPassword = params.oldPassword
        String newPassword = params.newPassword
        System.out.println("-------# 1=" + oldPassword)
        if (oldPassword.length() < 60 || newPassword.length() < 60) {
            log.debug("Authentication failed: no credentials provided2")
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'AbstractUserDetailsAuthenticationProvider.badCredentials', default: "Bad credentials")
                    redirect action: "index", method: "GET"
                }
                '*' { render status: NO_CONTENT }
            }
            return
        }
        oldPassword = oldPassword.substring(oldPassword.length() - 31)
        newPassword = newPassword.substring(newPassword.length() - 31)
        System.out.println("-------# 1=" + oldPassword)
        System.out.println("-------# 1=" + newPassword)
        User user = User.findByAccount(userDetails.username)
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(5);
        if (!bCryptPasswordEncoder.matches(oldPassword, user.pd)) {
            log.debug("Authentication failed: password does not match stored value3")
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'AbstractUserDetailsAuthenticationProvider.badCredentials', default: "Bad credentials")
                    redirect action: "index", method: "GET"
                }
                '*' { render status: NO_CONTENT }
            }
            return
        }
        User.withTransaction {
            user.pd = bCryptPasswordEncoder.encode(newPassword)
            user.save()
        }

        System.out.println("-------# is true")
        redirect uri: '/'
    }

    def toChangePassword() {
        render view: '/md/user/changePassword'
    }

    def changePassword() {
        System.out.println(params)
        String newPassword = params.newPassword
        System.out.println("-------# 1=" + newPassword)
        if (newPassword.length() < 60) {
            log.debug("Authentication failed: no credentials provided2")
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'AbstractUserDetailsAuthenticationProvider.badCredentials', default: "Bad credentials")
                    render view: '/md/user/changePassword'
                }
            }
            return
        }
        newPassword = newPassword.substring(newPassword.length() - 31)
        System.out.println("-------# 1=" + newPassword)
        User user = User.findByAccount(params.username)
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(5);
        User.withTransaction {
            user.pd = bCryptPasswordEncoder.encode(newPassword)
            user.save()
        }

        System.out.println("-------# is true")
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'xx.xx', default: "Change Successful")
                render view: '/md/user/changePassword'
            }
        }
        return
    }

    private Map getGifCaptcha() {
        com.easecurity.core.captcha.GifCaptcha gifCaptcha = new com.easecurity.core.captcha.GifCaptcha(130, 48, gifCaptchaLength, gifCaptchaDelay)
        String key = UUID.randomUUID().toString()
        String verCode = gifCaptcha.text().toLowerCase()
        GifCaptcha dDifCaptcha1 = new GifCaptcha()
        dDifCaptcha1.sessionId = getSession().getId()
        dDifCaptcha1.key2 = key
        dDifCaptcha1.value = verCode
        dDifCaptcha1.validTime = System.currentTimeMillis() + validTime
        GifCaptcha.withTransaction {
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
}
