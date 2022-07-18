/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 控制方法访问权限。使用“@EaSecured("allUser")”为所有登录用户可访问。 多条件时使用“or”关系。
 */
@Target(METHOD)
@Retention(RUNTIME)
@Inherited
@Documented
@EaSecured
public @interface EaSecuredIP {
    String value() default "";
}
