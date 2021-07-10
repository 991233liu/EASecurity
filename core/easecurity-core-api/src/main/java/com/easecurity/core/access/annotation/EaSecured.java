/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 控制方法访问权限。使用“@EaSecured("allUser")”为所有登录用户可访问。
 * 多条件时使用“or”关系。
 */
@Target(METHOD)
@Retention(RUNTIME)
@Inherited
@Documented
// TODO 支持正则表达式？
// TODO 支持其它授权方式
// TODO 类上注解，全部启用？
public @interface EaSecured {
    
    String value() default "";

    /**
     * 基于组织的访问控制。格式：{'id' : ['1','2'], 'code' : ['gongsi', 'abumen']}。 支持的属性：id、code、name、fullName
     */
    String org() default "";
    
    /**
     * 授权方式，详见{@link EasType}
     */
    EasType type() default EasType.DATABASE_AND_SOURCE;
}
