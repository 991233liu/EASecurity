/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.redis;

import java.util.Map;

/**
 * 多Redis支持，副Redis配置
 *
 */
public class OtherRedisProperties {

    private Map<String, CustomRedisProperties> datasources;

    public Map<String, CustomRedisProperties> getDatasources() {
        return datasources;
    }

    public void setDatasources(Map<String, CustomRedisProperties> datasources) {
        this.datasources = datasources;
    }


}
