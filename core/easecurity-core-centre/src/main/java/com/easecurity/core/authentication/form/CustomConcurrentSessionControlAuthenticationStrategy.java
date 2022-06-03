/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication.form;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.util.Assert;

import com.easecurity.core.authentication.LogoutService;
import com.easecurity.core.utils.BeanUtils;

/**
 * 并发登录时，被踢掉的用户需要通知远程系统
 */
public class CustomConcurrentSessionControlAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy {

    private SessionRepository<?> sessionRepository;
    private LogoutService logoutService;
    private List<String> logoutUrls;

    public CustomConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
	super(sessionRegistry);
    }

    @Override
    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions, SessionRegistry registry) throws SessionAuthenticationException {
	if (sessionRepository == null)
	    sessionRepository = (SessionRepository<?>) BeanUtils.getBean("sessionRepository");
	if (logoutService == null)
	    logoutService = (LogoutService) BeanUtils.getBean("logoutService");
	// 新登录的session会把旧的session踢掉，被踢掉session需要通知远端服务器，进行退出登录操作
	sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
	int maximumSessionsExceededBy = sessions.size() - allowableSessions + 1;
	List<SessionInformation> sessionsToBeExpired = sessions.subList(0, maximumSessionsExceededBy);
	for (SessionInformation session : sessionsToBeExpired) {
	    Session session1 = sessionRepository.findById(session.getSessionId());
	    if (session1 != null && logoutUrls != null && !logoutUrls.isEmpty()) {
		String jti = session1.getAttribute("JWT.jti");
		logoutService.asynLogoutByCookie(logoutUrls, jti);
	    }
	}
	super.allowableSessionsExceeded(sessions, allowableSessions, registry);
    }

    public void setMessageSource(MessageSourceAccessor messageSource) {
	Assert.notNull(messageSource, "messageSource cannot be null");
	this.messages = messageSource;
    }

    public List<String> getLogoutUrls() {
	return logoutUrls;
    }

    public void setLogoutUrls(List<String> logoutUrls) {
	this.logoutUrls = logoutUrls;
    }
}
