package com.easecurity.admin;

import java.lang.annotation.*;

/**
 * 让自动化脚本中自动处理i18n
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface I18nSource {
    String value();
}
