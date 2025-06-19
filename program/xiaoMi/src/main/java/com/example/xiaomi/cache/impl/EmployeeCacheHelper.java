package com.example.xiaomi.cache.impl;


import com.example.xiaomi.cache.AbstractCacheHelper;
import com.example.xiaomi.entity.Employee;
import com.example.xiaomi.redis.IRedisService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmployeeCacheHelper extends AbstractCacheHelper<Integer, Employee> {
    @Autowired
    public EmployeeCacheHelper(
            @Qualifier("employCache") Cache<Integer, Employee> localCache,
            IRedisService redisService
    ) {
        super(localCache, redisService);
    }
}