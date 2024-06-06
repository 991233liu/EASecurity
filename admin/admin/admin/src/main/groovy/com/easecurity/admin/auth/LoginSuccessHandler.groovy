package com.easecurity.admin.auth

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.session.SessionRepository

/**
 * 登录成功后消息和跳转处理类。处理逻辑：
 * <p>
 * <ul>
 * <li>如果请求中带有“redirect_url”参数，则直接跳转此参数表示的链接；</li>
 * <li>如果什么参数也没有，则按照{@link SavedRequestAwareAuthenticationSuccessHandler}处理</li>
 * </ul>
 */
class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private SessionRepository<?> sessionRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        UserDetails principal = ServletUtils.currentUser
//        User user  = new User()
//		user.username = principal.username
//		user.fullName = principal.fullName
//		user.avatar = principal.avatar
//		user.email = principal.email
//		user.lastLoginTime = principal.lastLoginTime
//		user.roles = principal.authorities*.authority
//		user.type = User.Type.ADMIN
//		ServletUtils.getSession().setAttribute("CURRENTUSER", user)
        String str = '{"code": "200", "message": "success"}'
//        Map map = [code: '200', message: "success"]
        response.setCharacterEncoding("utf-8")
        response.setHeader("Content-Type", "application/json ")
        response.getWriter().write(str)
//        response map
//        render view: '/message', model: []
    }
}
