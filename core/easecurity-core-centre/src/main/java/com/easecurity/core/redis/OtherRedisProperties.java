package com.easecurity.core.redis;

import java.util.Map;

/**
 * 多Redis支持，副Redis配置
 *
 */
public class OtherRedisProperties {

    private Map<String, CustomRedisProperties> datasources;
    private boolean enable;

    public Map<String, CustomRedisProperties> getDatasources() {
        return datasources;
    }

    public void setDatasources(Map<String, CustomRedisProperties> datasources) {
        this.datasources = datasources;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
