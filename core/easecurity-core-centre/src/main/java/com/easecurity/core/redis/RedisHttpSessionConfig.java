/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.redis;

//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.session.data.redis.RedisIndexedSessionRepository;
//import org.springframework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;
//import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.StringUtils;

/**
 * RedisSession配置
 *
 */
@Configuration
public class RedisHttpSessionConfig {
//    public class RedisHttpSessionConfig extends RedisHttpSessionConfiguration {

    @Bean
    public CookieSerializer cookieSerializer() {
	DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
	defaultCookieSerializer.setCookiePath("/");
	defaultCookieSerializer.setCookieName("SESSION_TEST");
	return defaultCookieSerializer;
    }

//    @Autowired
//    public void setRedisConnectionFactory(@SpringSessionRedisConnectionFactory ObjectProvider<RedisConnectionFactory> springSessionRedisConnectionFactory,
//	    ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
//	RedisConnectionFactory redisConnectionFactoryToUse = springSessionRedisConnectionFactory.getIfAvailable();
//	if (redisConnectionFactoryToUse == null) {
//	    redisConnectionFactoryToUse = redisConnectionFactory.getObject();
//	}
//	this.redisConnectionFactory = redisConnectionFactoryToUse;
//    }
//    @Bean(name = "sessionRedisOperations")
////    @Override
//    public RedisTemplate<Object, Object> sessionRedisOperations(RedisConnectionFactory connectionFactory) {
//	LettuceConnectionFactory lc = (LettuceConnectionFactory) connectionFactory;
//	RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//	lc.setDatabase(3);
//	template.setKeySerializer(new StringRedisSerializer());
////       template.setHashKeySerializer(new StringRedisSerializer());
////       if (this.defaultRedisSerializer != null) {
////          template.setDefaultSerializer(this.defaultRedisSerializer);
////       }
////       connectionFactory.
//	template.setConnectionFactory(lc);
////       template.set
//	template.afterPropertiesSet();
//	return template;
//    }
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(@SpringSessionRedisConnectionFactory ObjectProvider<RedisConnectionFactory> springSessionRedisConnectionFactory,
//	    ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
//	RedisConnectionFactory redisConnectionFactoryToUse = springSessionRedisConnectionFactory.getObject();
//	if (redisConnectionFactoryToUse == null) {
////	    redisConnectionFactoryToUse = redisConnectionFactory.getObject();
//	    redisConnectionFactoryToUse = (RedisConnectionFactory) redisConnectionFactory;
////	    redisConnectionFactoryToUse = redisConnectionFactory
//	}
//	if (ClassUtils.isPresent("io.lettuce.core.RedisClient", null) && redisConnectionFactoryToUse instanceof LettuceConnectionFactory) {
////		return ((LettuceConnectionFactory) redisConnectionFactoryToUse).getDatabase();
//	    ((LettuceConnectionFactory) redisConnectionFactoryToUse).setDatabase(2);
//	}
//	if (ClassUtils.isPresent("redis.clients.jedis.Jedis", null) && redisConnectionFactoryToUse instanceof JedisConnectionFactory) {
//
//	}
//
//	return redisConnectionFactoryToUse;
//    }
//    @Bean
//    public RedisIndexedSessionRepository sessionRepository(RedisConnectionFactory redisConnectionFactory) {
//	RedisTemplate<Object, Object> redisTemplate = createRedisTemplate(redisConnectionFactory);
//	RedisIndexedSessionRepository sessionRepository = new RedisIndexedSessionRepository(redisTemplate);
////	sessionRepository.setApplicationEventPublisher(this.applicationEventPublisher);
////	if (this.indexResolver != null) {
////	    sessionRepository.setIndexResolver(this.indexResolver);
////	}
////	if (this.defaultRedisSerializer != null) {
////	    sessionRepository.setDefaultSerializer(this.defaultRedisSerializer);
////	}
////	sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);
////	if (StringUtils.hasText(this.redisNamespace)) {
////	    sessionRepository.setRedisKeyNamespace(this.redisNamespace);
////	}
////	sessionRepository.setFlushMode(this.flushMode);
////	sessionRepository.setSaveMode(this.saveMode);
////	int database = resolveDatabase();
////	sessionRepository.setDatabase(database);
////	this.sessionRepositoryCustomizers.forEach((sessionRepositoryCustomizer) -> sessionRepositoryCustomizer.customize(sessionRepository));
//	return sessionRepository;
//    }
//
//    private RedisTemplate<Object, Object> createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//	RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//	redisTemplate.setKeySerializer(new StringRedisSerializer());
//	redisTemplate.setHashKeySerializer(new StringRedisSerializer());
////	if (this.defaultRedisSerializer != null) {
////	    redisTemplate.setDefaultSerializer(this.defaultRedisSerializer);
////	}
//	redisTemplate.setConnectionFactory(redisConnectionFactory);
////	redisTemplate.setBeanClassLoader(this.classLoader);
//	redisTemplate.afterPropertiesSet();
//	return redisTemplate;
//    }
}
