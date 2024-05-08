/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.distributedid;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 分布式id生成方法。<br>
 * 基于“雪花算法”。采用64bit。在使用雪花算法时，由于生成的ID是64位，在传递给前端时，需要考虑以字符串的类型进行传递，
 * 否则可能会导致前端类型溢出，再回传到服务器时已经变成另外一个值。这是因为Number类型的ID在JS中最大只支持53位，直接将雪花算法的生成的ID传递给JS，会导致溢出。<br>
 * web项目多实例部署情况下，每个实例的机器码应该是不同的。否则，多实例间有可能会出现重复的id。
 * <li>此64位ID格式：1位标识 + 42位时间戳差值标识 + x位模块标识 + "10-x"位机器码 + 11位毫秒内序列号标识;</li>
 * <li>时间戳对应42位二进制,可使用139年；</li>
 * <li>11位序列每秒理论上可有2048*1000的并发。</li>
 * <p>
 * 
 */

public class SnowFlake {
    private static Logger log = LoggerFactory.getLogger(SnowFlake.class);

    /**
     * 开始时间截 1656644048443 是 (Jan 01 00:00:00 UTC 2022)
     */
    private static final long startTime = 1656644048443L;

    /**
     * 业务标识所占位数
     */
    private long businessIdBits = 6L;

    /**
     * 随机数所占位数
     */
    private long machineIdBits = 4L;

    /**
     * 支持的最大业务标识id
     */
    private long maxBusinessId = -1L ^ (-1L << businessIdBits);

    /**
     * 支持的最大随机数
     */
    private long maxMachineId = -1L ^ (-1L << machineIdBits);

    /**
     * 毫秒内序列id所占位数
     */
    private long sequenceBits = 11L;

    /**
     * 随机数向左移的位数
     */
    private long machineIdLeftShift = sequenceBits;

    /**
     * 业务标识id向左移的位数
     */
    private long businessIdLeftShift = machineIdBits + machineIdLeftShift;

    /**
     * 时间截向左移的位置
     */
    private long timestampLeftShift = businessIdBits + businessIdLeftShift;

    /**
     * 生成序列的掩码
     */
    private long sequenceMask = -1 ^ (-1 << sequenceBits); // 2047

    /**
     * 初始业务标识id
     */
    private long businessId;

    /**
     * 初始随机数
     */
    private long machineId;

    /**
     * 同一个时间截内生成的序列数，初始值是0
     */
    private long sequence = 0L;

    /**
     * 上次生成id的时间截
     */
    private long lastTimestamp = -1L;

    private SnowFlake(long businessId, long businessIdLength, long machineId) {
	if (businessIdLength < 0 || businessIdLength > 10) {
	    throw new IllegalArgumentException(String.format("businessIdLength [%d] 小于0或者大于最大businessIdLength [10] !", businessId, 10));
	}
	initialize(businessIdLength, 10 - businessIdLength);
	if (businessId < 0 || businessId > maxBusinessId) {
	    throw new IllegalArgumentException(String.format("业务标识id [%d] 小于0或者大于最大业务标识id [%d] !", businessId, maxBusinessId));
	}
	if (machineId < 0 || machineId > maxMachineId) {
	    throw new IllegalArgumentException(String.format("机器码 [%d] 小于0或者大于最大机器码 [%d] !", machineId, maxMachineId));
	}
	this.businessId = businessId;
	if (businessIdLength < 10)
	    this.machineId = machineId;
    }

    private void initialize(long businessIdBits, long machineIdBits) {
	/**
	 * 业务标识所占位数
	 */
	this.businessIdBits = businessIdBits;

	/**
	 * 随机数所占位数
	 */
	this.machineIdBits = machineIdBits;
	/**
	 * 支持的最大业务标识id
	 */
	maxBusinessId = -1L ^ (-1L << businessIdBits);

	/**
	 * 支持的最大随机数
	 */
	maxMachineId = -1L ^ (-1L << machineIdBits);
	/**
	 * 随机数向左移的位数
	 */
	machineIdLeftShift = sequenceBits;

	/**
	 * 业务标识id向左移的位数
	 */
	businessIdLeftShift = machineIdBits + machineIdLeftShift;

	/**
	 * 时间截向左移的位置
	 */
	timestampLeftShift = businessIdBits + businessIdLeftShift;
    }

    /**
     * web项目多实例部署下，每个实例的机器码应该是不同的。否则，多实例间有可能会出现重复的id。
     * 
     * @param businessId       业务标识
     * @param businessIdLength 最大业务标识个数(应该为2的整数倍)
     * @param machineId        机器码
     * @return
     */
    synchronized static final SnowFlake getInstance(long businessId, int businessIdLength, long machineId) {
	int len = businessIdLength % 2;
	if (len > 0 || businessIdLength < 1 || businessIdLength > 1024)
	    throw new IllegalArgumentException(String.format("businessIdLength应为2的整数倍，且大于0小于等于1024"));
	else {
	    while (++len <= 10) {
		int p = (int) Math.pow(2, len);
		if ((p == businessIdLength) || p > businessIdLength)
		    break;
	    }
	}
	return new SnowFlake(businessId, len, machineId);
    }

    /**
     * 生成id
     * 
     * @return 分布式id
     */
    public synchronized long nextId() {
	long timestamp = timeGen();
	if (timestamp < lastTimestamp) {
	    throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
	}
	// 表示是同一时间截内生成的id
	if (timestamp == lastTimestamp) {
	    sequence = (sequence + 1) & sequenceMask;
	    // 说明当前时间生成的序列数已达到最大
	    if (sequence == 0) {
		// 生成下一个毫秒级的序列
		timestamp = tilNextMillis();
		// 序列从0开始
		sequence = 0L;
	    }
	} else {
	    // 新的时间截，则序列从0开始
	    sequence = 0L;
	}

	lastTimestamp = timestamp;

	if (machineIdBits == 0)
	    return ((timestamp - startTime) << timestampLeftShift) // 时间截部分
		    | (businessId << businessIdLeftShift) // 业务标识部分
		    | sequence; // 序列部分
	else
	    return ((timestamp - startTime) << timestampLeftShift) // 时间截部分
		    | (businessId << businessIdLeftShift) // 业务标识部分
		    | (machineId << machineIdLeftShift) // 随机数部分
		    | sequence; // 序列部分
    }

    /**
     * 获取下一毫秒数
     * 
     * @return timestamp
     */
    private long tilNextMillis() {
	long timestamp = timeGen();
	while (timestamp <= lastTimestamp) {
	    timestamp = timeGen();
	}
	return timestamp;
    }

    /**
     * 当前毫秒数
     * 
     * @return 系统毫秒数
     */
    private long timeGen() {
	return System.currentTimeMillis();
    }

    /**
     * 从分布式id中反推出id生成时间（毫秒数）
     * 
     * @param id 分布式id
     * @return long
     */
    public long getCreateTime(long id) {
	long diffTime = getFlagBit(id, Long.toBinaryString(id).length(), timestampLeftShift);
	long createMillis = diffTime + startTime;
	return createMillis;
    }

    /**
     * 从分布式id中反推出业务标识id
     * 
     * @param id 分布式id
     * @return long
     */
    public long getCreateBusinessId(long id) {
	return getFlagBit(id, timestampLeftShift, businessIdLeftShift);
    }

    /**
     * 从分布式id反推出随机数
     * 
     * @param id 分布式id
     * @return long
     */
    public long getCreateMachineId(long id) {
	if (machineIdBits == 0)
	    return -1;
	return getFlagBit(id, businessIdLeftShift, machineIdLeftShift);
    }

    /**
     * 此64位ID格式：1位标识 + 42位时间戳差值标识 + x位模块标识 + "10-x"位随机数 + 11位毫秒内序列号标识;
     * 
     * <pre>
     * Examples: id.getFlagBit(timestampLeftShift, businessIdLeftShift) returns
     * long型网省标识
     * 
     * <pre>
     * 
     * @param id             分布式id
     * @param beginLeftShift 起始位左移位数
     * @param endLeftShift   结束位左移位数
     * @return long
     */
    private long getFlagBit(long id, long beginLeftShift, long endLeftShift) {
	long flagBit = 0L;
	try {
	    String idBinaryStr = Long.toBinaryString(id);
	    int idBinaryStrLength = idBinaryStr.length();
	    String flagBitBinary = idBinaryStr.substring(idBinaryStrLength - (int) beginLeftShift, idBinaryStrLength - (int) endLeftShift);
	    flagBit = Long.valueOf(flagBitBinary, 2);
	} catch (Exception e) {
	    log.error("非法的分布式id参数！ id：" + id, e);
	    throw new IllegalArgumentException("Illegal Distribute Id！id： " + id, e);
	}
	return flagBit;
    }

    public static void main(String[] args) {
	SnowFlake df = SnowFlake.getInstance(127, 128, 8);
	long aa = System.currentTimeMillis();
	for (int i = 0; i < 20000; i++) {
//	    System.out.println((df.nextId()));
//	    df.nextId();
	    long id = df.nextId();
	    System.out.println(id + "," + df.getCreateTime(id));
	}
	System.out.println("一万次执行时间：" + (System.currentTimeMillis() - aa));
	long id = df.nextId();
	System.out.println(id + "," + df.getCreateTime(id));
	System.out.println(id + "," + df.getCreateBusinessId(id));
	System.out.println(id + "," + df.getCreateMachineId(id));

	// 42位二进制最小值
	String minTimeStampStr = "000000000000000000000000000000000000000000";
	// 42位二进制最大值
	String maxTimeStampStr = "111111111111111111111111111111111111111111";
	// 转10进制
	long minTimeStamp = new BigInteger(minTimeStampStr, 2).longValue();
	long maxTimeStamp = new BigInteger(maxTimeStampStr, 2).longValue();
	// 一年总共多少毫秒
	long oneYearMills = 1L * 1000 * 60 * 60 * 24 * 365;
	// 算出最大可以多少年
	System.out.println((maxTimeStamp - minTimeStamp) / oneYearMills);
    }
}
