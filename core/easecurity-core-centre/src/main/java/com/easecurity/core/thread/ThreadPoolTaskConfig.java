/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.thread;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    /**
     * 核心线程数（默认线程数）
     */
    @Value("${easecurity.async.task.corePoolSize:5}")
    private int corePoolSize;
    /**
     * 最大线程数
     */
    @Value("${easecurity.async.task.maxPoolSize:30}")
    private int maxPoolSize;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    @Value("${easecurity.async.task.keepAliveTime:30}")
    private int keepAliveTime;
    /**
     * 缓冲队列大小
     */
    @Value("${easecurity.async.task.queueCapacity:1024}")
    private int queueCapacity;
    /**
     * 线程池名前缀
     */
    private String threadNamePrefix = "task-";

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
