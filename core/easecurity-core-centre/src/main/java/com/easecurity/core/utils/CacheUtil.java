/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.utils;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.redis.RedisUtil;

/**
 * 缓存统一管理服务。系统中所有session缓存和Redis缓存，统一从这里进出。 运维时，可使用此类统一运维。
 */
public class CacheUtil {

    private static RedisUtil redisUtil = (RedisUtil) BeanUtils.getBean(RedisUtil.class);
    @SuppressWarnings("unchecked")
    private static RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) BeanUtils.getBean("redisTemplate");
    private static int accessTokenValidTime = ((LoginService) BeanUtils.getBean(LoginService.class)).getAccessTokenValidTime();

    /**
     * 将数据存入session中。此类数据在退出登录或者session失效后自动清除。
     * 
     * @param key
     * @param value
     * @return 如果当前用户未登录，false；反之，true。
     */
    public static boolean setSessionCache(String key, Object value) {
	HttpSession session = ServletUtils.getSession();
	if (session != null) {
	    session.setAttribute(key, value);
	    return true;
	}
	return false;
    }

    /**
     * 从session中获取数据。如果当前用户未登录，则返回false。
     * 
     * @param key
     * @return
     */
    public static Object getSessionCache(String key) {
	HttpSession session = ServletUtils.getSession();
	if (session != null)
	    return session.getAttribute(key);
	return null;
    }

    /**
     * 将数据存入AccessTokenCache中。此类数据在退出登录或者session失效后自动清除。
     * 
     * @param key
     * @param value
     * @return 如果当前用户未登录，false；反之，true。
     */
    public static boolean setAccessTokenCache(String key, Object value) {
	String at = ServletUtils.getAccessToken();
	if (at != null) {
	    return redisUtil.set("at:" + at + ":" + key, value, accessTokenValidTime);
	}
	return false;
    }

    /**
     * 从AccessTokenCache中获取数据。如果当前用户未登录，则返回false。
     * 
     * @param key
     * @return
     */
    public static Object getAccessTokenCache(String key) {
	String at = ServletUtils.getAccessToken();
	if (at != null) {
	    return redisUtil.get("at:" + at + ":" + key);
	}
	return null;
    }

    /**
     * 清除accessToken下所有缓存数据。
     * 
     * @param accessToken
     * @return
     */
    public static void delAccessTokenCache(String accessToken) {
	if (accessToken == null)
	    return;
	Set<String> keys = redisTemplate.keys("at:" + accessToken + ":" + "*");
	if (!CollectionUtils.isEmpty(keys)) {
	    redisTemplate.delete(keys);
	}
    }

    /**
     * 将数据存入Cache中。
     * 
     * @param key
     * @param value
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean setCache(String key, Object value, long time) {
	return redisUtil.set("cc:" + key, value);
    }

    /**
     * 从Cache中获取数据。
     * 
     * @param key
     * @return
     */
    public static Object getCache(String key) {
	return redisUtil.get("cc:" + key);
    }

    /**
     * 从Cache中删除数据。
     * 
     * @param key
     * @return
     */
    public static void delCache(String key) {
	redisUtil.del(new String[] { "cc:" + key });
    }
}
