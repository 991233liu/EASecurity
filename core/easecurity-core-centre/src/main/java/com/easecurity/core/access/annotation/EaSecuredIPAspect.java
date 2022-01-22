/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.easecurity.core.utils.ServletUtils;

/**
 * 控制方法访问权限。 多条件时默认使用“or”关系。
 *
 */
@Aspect
@Component
public class EaSecuredIPAspect {
    private static final Logger log = LoggerFactory.getLogger(EaSecuredIPAspect.class);

    Integer validTime;
    @Value("${easecurity.client.ips}")
    List<String> ips;

    @Pointcut("@annotation(com.easecurity.core.access.annotation.EaSecuredIP)")
    private void controllerMethod() {
	// @Pointcut定义的是切点
	System.out.println("这是自定义的切点");
    }

    /**
     * 检查是否有权限执行
     */
    @Around("controllerMethod()")
    public Object controllerMethodAround(ProceedingJoinPoint pjp) {
	Object result = null;
	try {
	    MethodSignature signature = (MethodSignature) pjp.getSignature();
	    Method method = signature.getMethod();
//	    String classFullName = method.getDeclaringClass().getName();
//	    String methodName = method.getName();
	    String methodSignature = method.toString();
	    EaSecuredIP eas = method.getAnnotation(EaSecuredIP.class);
//	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//	    String uri = request.getRequestURI();
	    String clientIp = ServletUtils.getClientIpAddr(ServletUtils.getRequest());
	    log.debug("controllerMethodAround, methodSignature={} eas={} clientIp={}", methodSignature, eas, clientIp);
	    if (ips.contains(clientIp)) {
		result = pjp.proceed();
	    } else {
		log.info("---## 很遗憾，权限校验未通过。你收到了一次非法请求，被请求方法为{}，clientIp为{}", methodSignature, clientIp);
	    }
	} catch (Exception e) {
	    log.error("controllerMethodAround 执行时出现异常：", e);
	} catch (Throwable e) {
	    log.error("controllerMethodAround 执行后续方法时出现异常：", e);
	}
	return result;
    }
}
