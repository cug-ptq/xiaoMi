package com.example.xiaomi.config;

import com.example.xiaomi.entity.Employee;
import com.example.xiaomi.entity.Product;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleGuavaCodeCacheConfig {

    // 商品库存缓存（Key: Long, Value: Integer）
    @Bean("productCache")
    public Cache<Integer, Product> productCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)     // 最大缓存数量
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 过期时间
                .build();
    }

    @Bean("employCache")
    public Cache<Integer, Employee> employCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)     // 最大缓存数量
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 过期时间
                .build();
    }
}