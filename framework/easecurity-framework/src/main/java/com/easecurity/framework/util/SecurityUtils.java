/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.util;

import java.time.Instant;
import java.util.Date;

import com.easecurity.distributedid.DistributeIdFactory;

/**
 * 由EASecurity提供的一些安全的高效工具类
 *
 */
public class SecurityUtils {

    // TODO 写到配置文件中
    private static int timeOut = 300;

//    /**
//     * 获取一个全局唯一的消息id，此id具有时效性，超时后将失效。 此id可以用于解决重放攻击
//     * 
//     * @param machineId 机器码（最小值0，最大值512）
//     * @return id
//     */
//    public static long getMessageId(long machineId) {
//	return DistributeIdFactory.getSnowFlakeWithMessageId(machineId).nextId();
//    }

    /**
     * 校验messageId是否还在有效期内
     * 
     * @param messageId
     * @return
     */
    public static boolean verifyMessageId(long messageId) {
	long time = DistributeIdFactory.getSnowFlakeWithMessageId(0).getCreateTime(messageId);
	if (Instant.now().isBefore(new Date(time - timeOut).toInstant()))
	    throw new RuntimeException(String.format("当前服务器时钟与xx时钟严重不一致，或者你受到了MessageId伪造攻击。MessageId的中的时间为：%d", new Date(time)));
	return Instant.now().isBefore(new Date(time + timeOut).toInstant());
    }

    /**
     * 校验messageId是否还在有效期内，且为指定的machineId产生的
     * 
     * @param messageId
     * @param machineId
     * @return
     */
    public static boolean verifyMessageId(long messageId, long machineId) {
	long mid = DistributeIdFactory.getSnowFlakeWithMessageId(0).getCreateMachineId(messageId);
	if (machineId != mid)
	    return false;
	long time = DistributeIdFactory.getSnowFlakeWithMessageId(0).getCreateTime(messageId);
	if (Instant.now().isBefore(new Date(time - timeOut).toInstant()))
	    throw new RuntimeException(String.format("当前服务器时钟与xx时钟严重不一致，或者你受到了MessageId伪造攻击。MessageId的中的时间为：%d", new Date(time)));
	return Instant.now().isBefore(new Date(time + timeOut).toInstant());
    }
}
