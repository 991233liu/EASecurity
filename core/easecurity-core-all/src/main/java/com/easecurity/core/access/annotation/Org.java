/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 基于组织的访问控制。目前支持id、code
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
// TODO 其它属性以后再实现吧
public @interface Org {

//    /**
//     * 组织ID。例如：{'1','2'}
//     */
//    String[] id() default {};
////    String[] name() default {};
////    String[] fullName() default {};
//    /**
//     * 组织编码。例如：{'gongsi', 'abumen'}
//     */
//    String[] code() default {};
////    String[] fullCode() default {};
}
