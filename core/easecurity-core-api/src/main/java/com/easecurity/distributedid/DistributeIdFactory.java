/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.distributedid;

import java.util.HashMap;

/**
 * 分布式id工厂类
 *
 */
public class DistributeIdFactory {

    private static volatile HashMap<String, SnowFlake> instances = new HashMap<>();
    private static volatile HashMap<String, SnowFlake> messageIdInstances = new HashMap<>();

    /**
     * web项目多实例部署下，每个实例的机器码应该是不同的。否则，多实例间有可能会出现重复的id。
     * 
     * @param businessId       业务标识
     * @param businessIdLength 最大业务标识个数(应该为2的整数倍)
     * @param machineId        机器码
     * @return
     */
    public static synchronized SnowFlake getSnowFlake(long businessId, int businessIdLength, long machineId) {
	String key = businessId + "," + machineId;
	if (instances.containsKey(key)) {
	    return instances.get(key);
	} else {
	    instances.put(key, SnowFlake.getInstance(businessId, businessIdLength, machineId));
	    return instances.get(key);
	}
    }

    /**
     * 返回一个专门用于生产MessageId的算法。
     * 
     * @param machineId        机器码（最小值0，最大值512）
     * @return
     */
    public static synchronized SnowFlake getSnowFlakeWithMessageId(long machineId) {
	String key = 0 + "," + machineId;
	if (messageIdInstances.containsKey(key)) {
	    return messageIdInstances.get(key);
	} else {
	    messageIdInstances.put(key, SnowFlake.getInstance(0, 1, machineId));
	    return messageIdInstances.get(key);
	}
    }
}
