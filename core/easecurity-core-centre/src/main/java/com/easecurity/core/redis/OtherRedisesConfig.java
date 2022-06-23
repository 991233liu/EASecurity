package com.easecurity.core.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * 多Redis支持，在“redises.datasources.xxx”下配置。
 * 副Redis可以在RedisUtil中指定Redis连接的名称“xxx”；或者在需用的service中注入：
 * 
 * @Resource Map<String, RedisTemplate<Object, Object>> otherRedises;
 *           Map的key是Redis连接的名称“xxx”
 */
@Configuration
public class OtherRedisesConfig implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(OtherRedisesConfig.class);
    /**
     * 配置信息的前缀
     */
    private static final String REDISES_PREFIX = "redises";

    private OtherRedisProperties otherRedisProperties;

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
    public Map<String, RedisSerializer<Object>> redisSerializer2(String redisName, CustomRedisProperties redisProperties) {
	Map<String, String> serializer = redisProperties.getSerializer();
	log.debug("----## {} Redis.serializer.key={}", redisName, serializer.get(SERIALIZER_KEY));

	HashMap<String, RedisSerializer<Object>> redisSerializer = new HashMap<String, RedisSerializer<Object>>();
	ObjectMapper om = objectMapper();
	RedisSerializer<Object> rs = null;
	rs = getRedisSerializer(serializer.get(SERIALIZER_KEY), om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_KEY, rs);
	rs = getRedisSerializer(serializer.get(SERIALIZER_HASHKEY), om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_HASHKEY, rs);
	rs = getRedisSerializer(serializer.get(SERIALIZER_VALUE), om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_VALUE, rs);
	rs = getRedisSerializer(serializer.get(SERIALIZER_HASHVALUE), om);
	if (rs != null)
	    redisSerializer.put(SERIALIZER_HASHVALUE, rs);
	return redisSerializer;
    }

    @Bean
    public Map<String, RedisTemplate<Object, Object>> otherRedises(RedisConnectionFactory redisConnectionFactory, Map<String, RedisSerializer<Object>> serializer,
	    ObjectProvider<RedisConnectionFactory> redisConnectionFactory2) {
	Map<String, RedisTemplate<Object, Object>> otherRedises = new HashMap<String, RedisTemplate<Object, Object>>();
	// 如果存在多Redis配置时，则建立多Redis链接
	if (otherRedisProperties != null && otherRedisProperties.getDatasources() != null && !otherRedisProperties.getDatasources().isEmpty()) {
	    otherRedisProperties.getDatasources().forEach((String k, CustomRedisProperties v) -> {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		Map<String, RedisSerializer<Object>> serializer1 = redisSerializer2(k, v);

		// 1. Redis配置
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(v.getHost());
		redisConfig.setPort(v.getPort());
		redisConfig.setDatabase(v.getDatabase());
		redisConfig.setPassword(RedisPassword.of(v.getPassword()));

		// 2. 连接池配置
		GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<Object>();
		// pool配置
		Lettuce lettuce = v.getLettuce();
		if (lettuce.getPool() != null) {
		    RedisProperties.Pool pool = v.getLettuce().getPool();
		    // 连接池最大连接数
		    poolConfig.setMaxTotal(pool.getMaxActive());
		    // 连接池中的最大空闲连接
		    poolConfig.setMaxIdle(pool.getMaxIdle());
		    // 连接池中的最小空闲连接
		    poolConfig.setMinIdle(pool.getMinIdle());
		    // 连接池最大阻塞等待时间（使用负值表示没有限制）
		    poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
		}
		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
		// timeout
		if (v.getTimeout() != null) {
		    builder.commandTimeout(v.getTimeout());
		}
		// shutdownTimeout
		if (lettuce.getShutdownTimeout() != null) {
		    builder.shutdownTimeout(lettuce.getShutdownTimeout());
		}
		// 创建Factory对象
		LettuceClientConfiguration clientConfig = builder.poolConfig(poolConfig).build();
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
		factory.afterPropertiesSet();
		redisTemplate.setConnectionFactory(factory);
		if (serializer1.containsKey(SERIALIZER_KEY))
		    redisTemplate.setKeySerializer(serializer1.get(SERIALIZER_KEY));// key序列化
		if (serializer1.containsKey(SERIALIZER_HASHKEY))
		    redisTemplate.setHashKeySerializer(serializer1.get(SERIALIZER_HASHKEY));// Hash key序列化
		if (serializer1.containsKey(SERIALIZER_VALUE))
		    redisTemplate.setValueSerializer(serializer1.get(SERIALIZER_VALUE));// value序列化
		if (serializer1.containsKey(SERIALIZER_HASHVALUE))
		    redisTemplate.setHashValueSerializer(serializer1.get(SERIALIZER_HASHVALUE));// Hash value序列化
		redisTemplate.afterPropertiesSet();
		otherRedises.put(k, redisTemplate);
	    });
	}
	return otherRedises;
    }

    @SuppressWarnings("rawtypes")
    private RedisSerializer getRedisSerializer(String serializerKey, ObjectMapper om) {
	if (serializerKey == null || serializerKey.isEmpty())
	    return null;
	RedisSerializer<Object> serializer = null;
	// TODO 后面再完善所有类型
	switch (serializerKey) {
	case "StringRedisSerializer":
	    return new StringRedisSerializer();
//		    break;
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
	    if (serializerKey.indexOf(".") > 0) {
		// TODO OemRedisSerializer

	    } else {
		// TODO 报错
	    }
	    break;
	}
	return serializer;
    }

    /**
     * 读取副Redis配置
     */
    @Override
    public void setEnvironment(Environment environment) {
	// 如果配置文件不包含相关配置，则不进行任何操作
	if (!environment.containsProperty(REDISES_PREFIX)) {
	    return;
	}
	otherRedisProperties = Binder.get(environment).bind(REDISES_PREFIX, OtherRedisProperties.class).get();
    }
}
