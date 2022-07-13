/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework;

/**
 * 客户端缓存管理服务。
 *
 */
public abstract class LocalStorageService {

    /**
     * 保存EaSecured中心数据到本地缓存
     * 
     * @param key
     * @param value
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    protected abstract void setCache(String key, Object value, long time);
}
