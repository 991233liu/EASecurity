/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * RedisSession配置
 *
 */
@EnableRedisHttpSession
@Configuration
public class RedisHttpSessionConfig {
}
