/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.access.annotation.EaSecuredAnonymous;
import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.framework.EaSecurityConfiguration;
import com.easecurity.framework.access.UriService;

/**
 * 认证Filter
 *
 */
public abstract class AbsWebSecurityFilter extends AbsAuthFilter {
    private static final Logger log = LoggerFactory.getLogger(AbsWebSecurityFilter.class);

    @Autowired
    private EaSecurityConfiguration eaSecurityConfiguration;
    @Autowired
    private UriService uriAccessService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ApplicationContext applicationContext;

    private RSAPublicKey rsaPublicKey;
    private List<String> anonymousUri = new ArrayList<>();

    @Override
    public EaSecurityConfiguration getConfig() {
        return eaSecurityConfiguration;
    }

    /**
     * 加载所有EaSecured注解管理的URI，进行进行初始化
     * 
     * @return
     */
    @Override
    public List<Method> loadAllUriWithAnnotation() {
        List<Method> allMethod = new ArrayList<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            Method method = m.getValue().getMethod();
            if (uriAccessService.isEaSecured(method))
                allMethod.add(method);
            if (uriAccessService.isEaSecuredAnonymous(method)) {
                // 一个方法可能对应多个url
                PatternsRequestCondition p = info.getPatternsCondition();
                if (p != null) {
                    for (String url : p.getPatterns()) {
                        anonymousUri.add(url);
                    }
                } else {
                    PathPatternsRequestCondition p2 = info.getPathPatternsCondition();
                    for (String url : p2.getPatternValues()) {
                        anonymousUri.add(url);
                    }

                }
            }
        }
        return allMethod;
//	Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);// 获取到 demo1Controller -> {Demo1Controller@5595}
//	for (Map.Entry<String, Object> entry : controllers.entrySet()) {// 遍历每个controller层
//	    System.out.println(entry.getKey());// demo1Controller
//	    Object value = entry.getValue();
//	    Class<?> aClass = AopUtils.getTargetClass(value);// 获取class
//	    System.out.println(aClass.isAnnotationPresent(RequestMapping.class));// true
//	    RequestMapping annotation = aClass.getAnnotation(RequestMapping.class);// 获取注解详情
//	    RequestMapping declaredAnnotation = aClass.getDeclaredAnnotation(RequestMapping.class);
//	    // 注解的详情可以直接调用了
//	    System.out.println(JSON.toJSONString(annotation));// {"path":[],"headers":[],"method":[],"name":"demo1con","produces":[],"params":[],"value":["/demo/demo1","/url111"],"consumes":[]}
//	    List<Method> methods = Arrays.asList(aClass.getMethods());// 获取方法
//	    List<Method> declaredMethods = Arrays.asList(aClass.getDeclaredMethods());// 获取方法
//	    System.out.println(declaredMethods.get(0).isAnnotationPresent(RequestMapping.class));// 判断这个方法有没有这个注解
//	    declaredMethods.forEach((it) -> {
//		System.out.println("---21:" + it.isAnnotationPresent(EaSecured.class));// 判断这个方法有没有这个注解
//		System.out.println("---22:" + JSON.toJSONString(it));
//		if (it.isAnnotationPresent(EaSecured.class))
//		    allMethod.add(it);
//	    });
//	    RequestMapping annotation1 = declaredMethods.get(0).getAnnotation(RequestMapping.class);// 获取方法的注解
//	    RequestMapping annotation2 = declaredMethods.get(0).getDeclaredAnnotation(RequestMapping.class);
//	    System.out.println(JSON.toJSONString(annotation1));// {"path":[],"headers":[],"method":[],"name":"方法1","produces":[],"params":[],"value":["/m1"],"consumes":[]}
//	}
    }

    /**
     * 保存EaSecured注解管理的URI到安全认证中心
     */
    @Override
    public void saveUri(Method method) {
        String classFullName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String methodSignature = method.toString();
        // 受注解控制的
        EaSecured[] teases = uriAccessService.getEaSecuredWithoutAnonymous(method);
        // 匿名访问的
        EaSecuredAnonymous easAnonymous = method.getAnnotation(EaSecuredAnonymous.class);
        List<String> urls = getAllUrl(method);
        for (String uri : urls) {
            uriAccessService.saveUriPermissions(teases, easAnonymous, uri, classFullName, methodName, methodSignature);
            log.debug("saveUri, uri={} methodSignature={} eas={}", uri, methodSignature, teases);
        }

        // TODO 启动后应该立即同步
    }

    private List<String> getAllUrl(Method method) {
        List<String> result = new ArrayList<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取所有url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod hMethod = m.getValue();
            if (method.getDeclaringClass().getName().equals(hMethod.getMethod().getDeclaringClass().getName()) && method.toString().equals(hMethod.getMethod().toString())) {
                // 一个方法可能对应多个url
                PatternsRequestCondition p = info.getPatternsCondition();
                if (p != null) {
                    for (String url : p.getPatterns()) {
                        result.add(url);
                    }
                } else {
                    PathPatternsRequestCondition p2 = info.getPathPatternsCondition();
                    for (String url : p2.getPatternValues()) {
                        result.add(url);
                    }

                }
                break;
            }
        }

        return result;
    }

    /**
     * 从远端认证中心获取当前登录用户的JWT
     * 
     * @param request
     * @return
     */
    @Override
    public JWT getCurrentUserJWTFromSecurityCentre(ServletRequest request) {
        try {
            if (rsaPublicKey == null)
                rsaPublicKey = getRSAPublicKey();
            long s = System.currentTimeMillis();
            JWT jwt = loginService.getCurrentUserJWT((HttpServletRequest) request, rsaPublicKey);
            if (log.isDebugEnabled())
                System.out.println("-----## 登录消耗时间为：" + (System.currentTimeMillis() - s));
            if (log.isDebugEnabled() && jwt != null && jwt.userDetails != null)
                System.out.println("-----## 当前登录人为：" + jwt.userDetails.account);
            return jwt;
        } catch (IOException e) {
            log.error("在处理JWT请求时，IO异常", e);
        } catch (JWTExpirationException e) {
            log.error("在处理JWT请求时，SecurityCentre返回的JWT无效", e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("在处理JWT请求时，RAS证书加载异常", e);
        }
        return null;
    }

    /**
     * 是否可匿名访问
     * 
     * @param uri
     * @param request
     * @return
     */
    @Override
    public boolean canAnonymousAccess(String uri, ServletRequest request) {
        if (anonymousUri.contains(uri))
            return true;
        return uri.endsWith("logout");
    }

    /**
     * 未登录时的处理。（远端认证中心没有返回有效的身份时的处理）
     * 
     * @param request
     * @param response
     * @param chain
     * @param jwt      远端认证中心返回的JWT
     */
    @Override
    public void noLogin(ServletRequest request, ServletResponse response, FilterChain chain, JWT jwt) {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            if (eaSecurityConfiguration.noLoginUrl != null && !"".equals(eaSecurityConfiguration.noLoginUrl)) {
                String uri = req.getRequestURI();
                // 如果是SecurityCentre跳转过来的请求，就不要再跳转回去了，避免死循环。
                if (uri.indexOf("/SecurityCentre/") < 0)
                    resp.sendRedirect(eaSecurityConfiguration.noLoginUrl);
                else
                    resp.setStatus(403);
            } else if (eaSecurityConfiguration.noLoginMessage != null && !"".equals(eaSecurityConfiguration.noLoginMessage)) {
                resp.getWriter().write(eaSecurityConfiguration.noLoginMessage);
                resp.setStatus(403);
            } else {
                String uri = req.getRequestURI();
                // 如果是SecurityCentre跳转过来的请求，就不要再跳转回去了，避免死循环。
                if (uri.indexOf("/SecurityCentre/") < 0)
                    resp.sendRedirect("/SecurityCentre/auth/login?redirect_url=" + URLEncoder.encode(uri, "GBK"));
                else
                    resp.setStatus(403);
            }
        } catch (Exception e) {
            log.error("在处理noLogin请求时，异常", e);
        }
    }

    public RSAPublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        File file = org.springframework.util.ResourceUtils.getFile(eaSecurityConfiguration.jwt.getPublicKey());
        if (file.isFile()) {
            return loginService.getRSAPublicKey(file);
        } else {
            return loginService.getRSAPublicKey(eaSecurityConfiguration.jwt.getPublicKey());
        }

    }
}
