/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.access.annotation;

import com.easecurity.db.BaseEnum;

/**
 * 授权方式
 *
 */
public enum EasType implements BaseEnum {

    /**
     * 默认配置，数据库和源码同时生效。如果源码中配置了授权，在admin平台运维时来自代码中的授权配置只能禁用不能修改。
     */
    DEFAULT("10", "默认"),
    /**
     * 只有数据库配置生效。此模式下，会把代码中的配置自动初始化到数据库中。
     */
    DATABASE_ONLY("20", "仅数据库"),
    /**
     * 只有代码配置生效。此模式下，会把代码中的配置自动初始化到数据库中，供查询、展示使用，不作为控制使用（可被禁用）。
     */
    SOURCE_ONLY("30", "仅代码");

    public final String id;
    public final String title;

    private EasType(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return this.id;
    }
}
