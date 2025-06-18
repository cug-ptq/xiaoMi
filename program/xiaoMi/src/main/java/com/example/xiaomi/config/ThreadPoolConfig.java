package com.example.xiaomi.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "executorService")
    public ExecutorService executorServiceA() {
        return new ThreadPoolExecutor(
                2,                       // corePoolSize
                2,                       // maxPoolSize
                60L, TimeUnit.SECONDS,   // keepAliveTime
                new ArrayBlockingQueue<>(5), // 有界队列，最多只能排队5个任务
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：直接抛异常
        );
    }
}
