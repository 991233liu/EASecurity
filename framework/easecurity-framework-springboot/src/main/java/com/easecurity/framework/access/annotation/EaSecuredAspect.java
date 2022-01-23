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
    UriService uriAccessService;
    @Resource
    LoginService loginService;

    @Resource
    EaSecurityConfiguration eaSecurityConfiguration;

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
	    EaSecureds eases = method.getAnnotation(EaSecureds.class);
	    if (eases == null) {
		EaSecured eas = method.getAnnotation(EaSecured.class);
		EaSecured[] teases = { eas };
		result = proceed(pjp, teases);
	    } else {
		result = proceed(pjp, eases.value());
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

    /**
     * 检查是否有权限执行（多个EaSecured之间是and关系）
     * 
     * @throws Throwable
     */
    @Around("clazzs()||clazz()")
    public Object classAround(ProceedingJoinPoint pjp) throws Throwable {
	Object result = null;
	try {
	    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
	    EaSecureds meases = method.getAnnotation(EaSecureds.class);
	    EaSecured meas = method.getAnnotation(EaSecured.class);
	    // 如果类和方法同时配置了@EaSecured，则使用方法的安全配置。
	    if (meases == null && meas == null) {
		EaSecureds eases = method.getDeclaringClass().getAnnotation(EaSecureds.class);
		if (eases == null) {
		    EaSecured eas = method.getDeclaringClass().getAnnotation(EaSecured.class);
		    EaSecured[] teases = { eas };
		    result = proceed(pjp, teases);
		} else {
		    result = proceed(pjp, eases.value());
		}
	    } else {
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
    private Object proceed(ProceedingJoinPoint pjp, EaSecured[] eases) throws Throwable {
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
		uriAccessService.saveUriPermissions(eases, uri, classFullName, methodName, methodSignature);
	    } catch (Exception e) {
		log.error("更新URI的授权信息时出现异常：", e);
	    }

	    validation = uriAccessService.validation(eases, uri, user, clientIp);
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

//    private boolean validation(ProceedingJoinPoint pjp) {
//	Object[] args = pjp.getArgs();
//	Object target = pjp.getTarget();
//	MethodSignature signature = (MethodSignature) pjp.getSignature();
//	Method method = signature.getMethod();
//	String methodSignature = method.toString();
//	String classFullName = method.toString();
//	String methodName = method.getName();
//	EaSecured eas = method.getAnnotation(EaSecured.class);
//	String org = eas.org();
//	String kind = pjp.getKind();
////	SourceLocation sourceLocation = (SourceLocation) pjp.getSourceLocation();
//	JoinPoint.StaticPart staticPart = pjp.getStaticPart();
//	// attributes可以获取request信息 session信息等
//	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//	HttpServletRequest request = attributes.getRequest();
//	UserDo userDo = (UserDo) request.getSession().getAttribute("userdo");
//	String uri = request.getRequestURI();
//	boolean[] flag = { false };
//	if (userDo != null)
//	    userDo.orgUsers.forEach(ou -> {
////		if (org.length == 0)
////		    return;
////		if (ou.id == Integer.parseInt(org[0]) || ou.id == Integer.parseInt(org[1]))
////		    flag[0] = true;
//	    });
//	return flag[0];
//    }

//    @Before(value = "pointCut()")
//    public void before(JoinPoint joinPoint) {
//	logger.info("@Before通知执行");
//	// 获取目标方法参数信息
//	Object[] args = joinPoint.getArgs();
//	Arrays.stream(args).forEach(arg -> { // 大大
//	    try {
//		logger.info(OBJECT_MAPPER.writeValueAsString(arg));
//	    } catch (JsonProcessingException e) {
//		logger.info(arg.toString());
//	    }
//	});
//
//	// aop代理对象
//	Object aThis = joinPoint.getThis();
//	logger.info(aThis.toString()); // com.xhx.springboot.controller.HelloController@69fbbcdd
//
//	// 被代理对象
//	Object target = joinPoint.getTarget();
//	logger.info(target.toString()); // com.xhx.springboot.controller.HelloController@69fbbcdd
//
//	// 获取连接点的方法签名对象
//	Signature signature = joinPoint.getSignature();
//	logger.info(signature.toLongString()); // public java.lang.String
//					       // com.xhx.springboot.controller.HelloController.getName(java.lang.String)
//	logger.info(signature.toShortString()); // HelloController.getName(..)
//	logger.info(signature.toString()); // String com.xhx.springboot.controller.HelloController.getName(String)
//	// 获取方法名
//	logger.info(signature.getName()); // getName
//	// 获取声明类型名
//	logger.info(signature.getDeclaringTypeName()); // com.xhx.springboot.controller.HelloController
//	// 获取声明类型 方法所在类的class对象
//	logger.info(signature.getDeclaringType().toString()); // class com.xhx.springboot.controller.HelloController
//	// 和getDeclaringTypeName()一样
//	logger.info(signature.getDeclaringType().getName());// com.xhx.springboot.controller.HelloController
//
//	// 连接点类型
//	String kind = joinPoint.getKind();
//	logger.info(kind);// method-execution
//
//	// 返回连接点方法所在类文件中的位置 打印报异常
//	SourceLocation sourceLocation = joinPoint.getSourceLocation();
//	logger.info(sourceLocation.toString());
//	// logger.info(sourceLocation.getFileName());
//	// logger.info(sourceLocation.getLine()+"");
//	// logger.info(sourceLocation.getWithinType().toString()); //class
//	// com.xhx.springboot.controller.HelloController
//
//	/// 返回连接点静态部分
//	JoinPoint.StaticPart staticPart = joinPoint.getStaticPart();
//	logger.info(staticPart.toLongString()); // execution(public java.lang.String
//						// com.xhx.springboot.controller.HelloController.getName(java.lang.String))
//
//	// attributes可以获取request信息 session信息等
//	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//	HttpServletRequest request = attributes.getRequest();
//	logger.info(request.getRequestURL().toString()); // http://127.0.0.1:8080/hello/getName
//	logger.info(request.getRemoteAddr()); // 127.0.0.1
//	logger.info(request.getMethod()); // GET
//
//	logger.info("before通知执行结束");
//    }
}
