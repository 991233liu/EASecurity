/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.access.annotation;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.access.annotation.EaSecureds;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.EaSecurityConfiguration;
import com.easecurity.framework.access.UriService;
import com.easecurity.framework.authentication.LoginService;
import com.easecurity.framework.utils.ServletUtils;

/**
 * 控制方法访问权限。 多条件时默认使用“or”关系。
 *
 */
@Aspect
@Component
public class EaSecuredAspect {
    private static final Logger log = LoggerFactory.getLogger(EaSecuredAspect.class);

    @Resource
    private UriService uriAccessService;
    @Resource
    private LoginService loginService;

    @Resource
    private EaSecurityConfiguration eaSecurityConfiguration;

    @Pointcut("@annotation(com.easecurity.core.access.annotation.EaSecureds)")
    private void methods() {
    }

    @Pointcut("@annotation(com.easecurity.core.access.annotation.EaSecured)")
    private void method() {
    }

    @Pointcut("@within(com.easecurity.core.access.annotation.EaSecureds)")
    private void clazzs() {
    }

    @Pointcut("@within(com.easecurity.core.access.annotation.EaSecured)")
    private void clazz() {
    }

    /**
     * 检查是否有权限执行
     * 
     * @throws Throwable
     */
    @Around("methods()||method()")
    public Object methodAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            // 受注解控制的
            EaSecured[] teases = uriAccessService.getEaSecuredWithoutAnonymous(method);
            // 匿名访问的
            EaSecuredAnonymous easAnonymous = method.getAnnotation(EaSecuredAnonymous.class);
            result = proceed(pjp, teases, easAnonymous);
        } catch (Exception e) {
            log.error("methodAround 执行时出现异常：", e);
            throw e;
        } catch (Throwable e) {
            log.error("methodAround 执行后续方法时出现异常：", e);
            throw e;
        }
        return result;
    }

    /**
     * 检查是否有权限执行
     * 
     * @throws Throwable
     */
    @Around("clazzs()||clazz()")
    public Object classAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        try {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            // 如果类和方法同时配置了@EaSecured或者@EaSecuredAnonymous，则使用方法的安全配置。
            if (!method.isAnnotationPresent(EaSecureds.class) && !method.isAnnotationPresent(EaSecured.class) && !method.isAnnotationPresent(EaSecuredAnonymous.class)) { // 使用类的配置
                // 受注解控制的
                EaSecured[] teases = uriAccessService.getEaSecuredWithoutAnonymous(method);
                // 匿名访问的
                EaSecuredAnonymous easAnonymous = method.getDeclaringClass().getAnnotation(EaSecuredAnonymous.class);
                result = proceed(pjp, teases, easAnonymous);
            } else { // 使用方法的配置
                result = pjp.proceed();
            }
        } catch (Exception e) {
            log.error("classAround 执行时出现异常：", e);
            throw e;
        } catch (Throwable e) {
            log.error("classAround 执行后续方法时出现异常：", e);
            throw e;
        }
        return result;
    }

    @SuppressWarnings("finally")
    private Object proceed(ProceedingJoinPoint pjp, EaSecured[] eases, EaSecuredAnonymous easAnonymous) throws Throwable {
        UserDetails user = null;
        String methodSignature = null;
        boolean validation = false;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            user = loginService.getLocalUserDetails(request.getSession());
            String clientIp = ServletUtils.getClientIpAddr(ServletUtils.getRequest());
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            String classFullName = method.getDeclaringClass().getName();
            String methodName = method.getName();
            methodSignature = method.toString();
            log.debug("proceed, methodSignature={} eas={} loginUser={} clientIp={}", methodSignature, eases, user, clientIp);

            try {
                uriAccessService.saveUriPermissions(eases, easAnonymous, uri, classFullName, methodName, methodSignature);
            } catch (Exception e) {
                log.error("更新URI的授权信息时出现异常：", e);
            }

            validation = uriAccessService.validation(eases, easAnonymous, uri, user, clientIp);
        } catch (Throwable e) {
            log.error("检查是否有权限执行时出现异常：", e);
        } finally {
            return proceed(pjp, validation, methodSignature, user);
        }
    }

    private Object proceed(ProceedingJoinPoint pjp, boolean validation, String methodSignature, UserDetails user) throws Throwable {
        Object result = null;
        if (validation) { // 有执行权限
            if (eaSecurityConfiguration.isDevelopmentMode()) { // 开发模式
                log.info("---## 恭喜你，权限校验通过。当前校验模式为{}", eaSecurityConfiguration.verification);
            }
            result = pjp.proceed();
        } else { // 无执行权限
            if (eaSecurityConfiguration.isDevelopmentMode()) { // 开发模式
                log.info("---## 很遗憾，权限校验未通过。你收到了一次非法请求，被请求方法为{}，当前登录人为{}，当前校验模式为{}", methodSignature, user, eaSecurityConfiguration.verification);
                result = pjp.proceed();
            } else {
                HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                httpServletResponse.setStatus(403);
                result = null;
            }
        }
        return result;
    }
}
