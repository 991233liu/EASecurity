/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

/**
 * 授权方式
 *
 */
public enum EasType {

    /**
     * 只使用数据库。此模式下，会把代码中的配置自动初始化到数据库中。
     */
    DATABASE_ONLY,
    /**
     * 使用数据库，但是来自代码中的授权配置只能禁用不能修改。
     */
    DATABASE_AND_SOURCE,
    /**
     * 只使用代码。此模式下，会把代码中的配置自动初始化到数据库中供查询、展示使用，不作为控制使用。但是可以从数据库中禁用某条授权，或者禁用整个URI。
     */
    SOURCE_ONLY
}