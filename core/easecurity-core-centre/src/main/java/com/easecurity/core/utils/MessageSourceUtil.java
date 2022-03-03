/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化消息。使用i18n
 *
 */
@Component
public class MessageSourceUtil {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code) {
	return getMessage(code, code);
    }

    public String getMessage(String code, String defaultMsg) {
	return getMessage(code, null, defaultMsg);
    }

    public String getMessage(String code, Object[] args, String defaultMsg) {
	return messageSource.getMessage(code, args, defaultMsg, getLocale());
    }

    private Locale getLocale() {
	Locale locale = null;
	HttpServletRequest request = ServletUtils.getRequest();
	if (request != null)
	    locale = request.getLocale();
	if (locale == null)
	    locale = LocaleContextHolder.getLocale();
	return locale;
    }
}
