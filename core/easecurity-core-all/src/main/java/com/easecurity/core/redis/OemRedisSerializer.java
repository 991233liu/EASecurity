/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.redis;

import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义Redis序列化方法
 *
 */
public interface OemRedisSerializer extends RedisSerializer<Object> {

    public void setObjectMapper(ObjectMapper objectMapper);
}
