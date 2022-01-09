/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.redis;

import java.util.Map;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;;

/**
 * 自定义的Redis属性类，增加了序列化类处理
 *
 */
public class CustomRedisProperties extends RedisProperties {
    
    private Map<String, String> serializer;

    public Map<String, String> getSerializer() {
        return serializer;
    }

    public void setSerializer(Map<String, String> serializer) {
        this.serializer = serializer;
    }
}
