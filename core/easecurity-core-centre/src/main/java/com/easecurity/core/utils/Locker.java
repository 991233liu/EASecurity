package com.easecurity.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * <p>
 * 利用数据库的持久化锁，业务锁<br>
 * <p>
 * 
 */
public class Locker {
    private static final Logger log = LoggerFactory.getLogger(Locker.class);

    private static JdbcTemplate jdbcTemplate;
    private static PlatformTransactionManager transactionManager;

    private static Locker instance = null;

    @SuppressWarnings("static-access")
    private Locker(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
	this.jdbcTemplate = jdbcTemplate;
	this.transactionManager = transactionManager;
    }

    public static synchronized final Locker getInstance(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
	if (instance == null)
	    instance = new Locker(jdbcTemplate, transactionManager);
	return instance;
    }

    private static final String sql_insert = "insert into s_locker (lkey, lock_time, timeout, timeout_time) values (?,NOW(6),?,NOW(6)+interval ?/1000 SECOND)";
    private static final String sql_exists = "select count(lkey) from s_locker where lkey = ? and timeout_time > NOW(6)";
    private static final String sql_delete = "delete from s_locker where lkey=?";
    private static final String sql_delete_timeout = "delete from s_locker where lkey=? and timeout_time <= NOW(6)";
    private static final String sql_delete_timeout_all = "delete from s_locker where timeout_time <= NOW(6)";

    /**
     * 判断是否被加锁，过期的锁不算。
     * 
     * @param key 锁名，每把锁的唯一关键字
     * @return true/false
     */
    public static boolean isLocked(String key) {
	int count = jdbcTemplate.queryForObject(sql_exists, Integer.class, key);
	return count > 0 ? true : false;
    }

    /**
     * 加锁。如果发现已被加锁，则直接返回false。不等待。
     * 
     * @param key     锁名，每把锁的唯一关键字
     * @param timeout 锁失效时间（毫秒），默认为30分钟，如果为null也等于采用默认时间
     * @return true则表示成功，false表示失败
     */
    public static boolean lockExist(String key, Integer timeout) {
	return lockExist(key, timeout, 0, null);
    }

    /**
     * 加锁
     * 
     * @param key          锁名，每把锁的唯一关键字
     * @param timeout      失效时间（毫秒），默认为30分钟，如果为null也等于采用默认时间
     * @param waitTime
     * 
     *                     <pre class="params">
     * 等待时间（毫秒），如果发现已加锁，是否等待解锁。
     *    默认为0即不等待；如果为大于0的整数，则最多等待该时间后返回；
     *    如果为负数，则一直等待：注意不要发生死锁，包括数据库链接此时也不会被释放的！
     *                     </pre>
     * 
     * @param waitInterval 等待过程中，轮询的时间间隔，单位为毫秒，默认为200ms
     * @return true则表示成功，false表示失败
     */
    public static boolean lockExist(String key, Integer timeout, Integer waitTime, Integer waitInterval) {
	try {
	    if (timeout == null)
		timeout = 1800000;
	    if (waitInterval == null)
		waitInterval = 200;
	    // 检测锁是否存在、存在则等待
	    boolean waitted = waitLock(key, waitTime, waitInterval, timeout);
	    // 锁不存在，则加锁后返回true
	    if (waitted) {
		TransactionStatus ts = null;
		try {
		    DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
		    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		    ts = transactionManager.getTransaction(def);
		    // 加锁前先清理已过期的锁，避免主键冲突
		    clearTimeoutLocks(key);
		    // 加锁
		    jdbcTemplate.update(sql_insert, key, timeout, timeout);
		} catch (Exception e) {
		    ts.setRollbackOnly();
		    throw e;
		} finally {
		    transactionManager.commit(ts);
		}
		return true;
	    } else
		return false;
	} catch (Exception se) {
	    // 多个应用同时执行时，由于未锁表，因此异步的执行顺序会导致加锁冲突，造成诸如“违反唯一约束条件“的异常
	    if (waitTime == 0)
		return false;
	    else {
		if (waitInterval > 0) {
		    try {
			Thread.sleep(waitInterval);
		    } catch (InterruptedException e) {
			log.error("业务锁执行异常", e);
		    }
		    if (waitTime > 0) {
			waitTime -= waitInterval;
			if (waitTime < 0)
			    waitTime = 0;
		    }
		}
		return lockExist(key, timeout, waitTime, waitInterval);
	    }
	}
    }

    /**
     * 等待锁释放
     * 
     * @return true则表示成功，false表示失败
     */
    private static boolean waitLock(String key, Integer waitTime, Integer waitInterval, Integer timeout) {
	try {
	    // 判断锁是否存在
	    if (isLocked(key)) {
		if (waitTime == 0)
		    return false; // 如果不等待，则直接返回
		// 等待解锁后重新获得锁
		Integer time = waitTime;
		while (waitTime < 0 || time > 0) {
		    Thread.sleep(waitInterval);
		    time -= waitInterval;
		    if (!isLocked(key))
			return true;
		}
	    } else
		return true;
	} catch (Exception e) {
	    log.error("业务锁执行异常", e);
	}
	return false;
    }

    /**
     * 解锁
     * 
     * @param key 锁名
     * @return true则表示成功，false表示失败
     */
    public static boolean unlock(String key) {
	TransactionStatus ts = null;
	try {
	    DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 事务定义类
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    ts = transactionManager.getTransaction(def);
	    jdbcTemplate.update(sql_delete, key);
	    return true;
	} catch (Exception e) {
	    ts.setRollbackOnly();
	    log.error("业务锁执行异常", e);
	} finally {
	    transactionManager.commit(ts);
	}
	return false;
    }

    /**
     * 清理已过期失效的锁
     */
    public static void clearTimeoutLocks() {
	try {
	    jdbcTemplate.update(sql_delete_timeout_all);
	} catch (Exception e) {
	    log.error("业务锁执行异常", e);
	}
    }

    /**
     * 清理已过期失效的锁
     * 
     * @param key 锁名
     */
    public static void clearTimeoutLocks(String key) {
	try {
	    jdbcTemplate.update(sql_delete_timeout, key);
	} catch (Exception e) {
	    log.error("业务锁执行异常", e);
	}
    }
}
