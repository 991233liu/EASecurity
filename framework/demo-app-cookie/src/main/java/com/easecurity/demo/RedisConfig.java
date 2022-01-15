/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.session.data.redis.RedisIndexedSessionRepository;
//import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;

/**
 * 定制化缓存配置
 *
 */
@Configuration
@EnableCaching // 启用缓存，这个注解很重要；
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.serializer.key:}")
    private String serializerKey = null;
    @Value("${spring.redis.serializer.hashkey:}")
    private String serializerHashkey = null;
    @Value("${spring.redis.serializer.value:}")
    private String serializerValue = null;
    @Value("${spring.redis.serializer.hashvalue:}")
    private String serializerHashvalue = null;

    private static final String SERIALIZER_KEY = "key";
    private static final String SERIALIZER_HASHKEY = "hashkey";
    private static final String SERIALIZER_VALUE = "value";
    private static final String SERIALIZER_HASHVALUE = "hashvalue";

    private ObjectMapper objectMapper() {
	ObjectMapper om = new ObjectMapper();
	om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
	om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	return om;
    }

    @SuppressWarnings("unchecked")
    @Bean
    public Map<String, RedisSerializer<Object>> redisSerializer(ObjectMapper om) {
	log.debug("----## spring.redis.serializer.key={}", serializerKey);

	HashMap<String, RedisSerializer<Object>> redisSerializer = new HashMap<String, RedisSerializer<Object>>();
	om = objectMapper();
	RedisSerializer<Object> rs = null;
	rs = getRedisSerializer(serializerKey, om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_KEY, rs);
	rs = getRedisSerializer(serializerHashkey, om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_HASHKEY, rs);
	rs = getRedisSerializer(serializerValue, om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_VALUE, rs);
	rs = getRedisSerializer(serializerHashvalue, om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_HASHVALUE, rs);
	return redisSerializer;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, Map<String, RedisSerializer<Object>> serializer,
	    ObjectProvider<RedisConnectionFactory> redisConnectionFactory2) {
	log.debug("----## spring.redis.serializer.key={}", serializer.get(SERIALIZER_KEY));

	RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
	redisTemplate.setConnectionFactory(redisConnectionFactory);
	if (serializer.containsKey(SERIALIZER_KEY))
	    redisTemplate.setKeySerializer(serializer.get(SERIALIZER_KEY));// key序列化
	if (serializer.containsKey(SERIALIZER_HASHKEY))
	    redisTemplate.setHashKeySerializer(serializer.get(SERIALIZER_HASHKEY));// Hash key序列化
	if (serializer.containsKey(SERIALIZER_VALUE))
	    redisTemplate.setValueSerializer(serializer.get(SERIALIZER_VALUE));// value序列化
	if (serializer.containsKey(SERIALIZER_HASHVALUE))
	    redisTemplate.setHashValueSerializer(serializer.get(SERIALIZER_HASHVALUE));// Hash value序列化
	redisTemplate.afterPropertiesSet();
	return redisTemplate;

    }

    @SuppressWarnings("rawtypes")
    private RedisSerializer getRedisSerializer(String serializerKey, ObjectMapper om) {
	if (serializerKey == null || serializerKey.isEmpty())
	    return null;
	RedisSerializer<Object> serializer = null;
	switch (serializerKey) {
	case "StringRedisSerializer":
	    return new StringRedisSerializer();
//	    break;
	case "GenericJackson2JsonRedisSerializer":
	    serializer = new GenericJackson2JsonRedisSerializer(om);
	    break;
	case "Jackson2JsonRedisSerializer":
	    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
	    jackson2JsonRedisSerializer.setObjectMapper(om);
	    serializer = jackson2JsonRedisSerializer;
	    break;

	default:
	    // TODO 自定义的
	    break;
	}
	return serializer;
    }
}
