/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制方法访问权限。
 * <p>
 * <ul>
 * <li>使用“@EaSecured("allUser")”为所有登录用户可访问。</li>
 * <li>如果类和方法同时配置了@EaSecured，则使用方法的安全配置。</li>
 * <li>同一个@EaSecured多条件时使用“or”关系；多个@EaSecured时使用“and”关系。</li>
 * </ul>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EaSecureds.class)
@Inherited
@Documented
// TODO 支持正则表达式？
public @interface EaSecured {
    
    String value() default "";

    /**
     * 基于组织的访问控制。格式：{'id' : ['1','2'], 'code' : ['gongsi', 'abumen']}。 支持的属性：id、code、fullCode、name、fullName
     */
    String org() default "";
    /**
     * 基于职务的访问控制。格式：{'id' : ['1','2'], 'code' : ['gongsilingdao', 'bumenlingdao']}。 支持的属性：id、code、name
     */
    String posts() default "";
    /**
     * 基于人员的访问控制。格式：{'id' : ['1','2'], 'account' : ['zhangsan', 'lisi']}。 支持的属性：id、account、name
     */
    String user() default "";
    /**
     * 基于小角色的访问控制。格式：{'id' : ['1','2'], 'code' : ['user', 'admin']}。 支持的属性：id、code、name、fullName
     */
    String role() default "";
    /**
     * 基于大角色的访问控制。格式：{'id' : ['1','2'], 'code' : ['user', 'admin']}。 支持的属性：id、code、name
     */
    String roleGroup() default "";
    /**
     * 基于IP的访问控制。
     */
    String[] IP() default {};
    
    /**
     * 授权方式，详见{@link EasType}
     */
    EasType type() default EasType.DEFAULT;
}
