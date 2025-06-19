package com.example.xiaomi.cache.impl;

import com.example.xiaomi.cache.AbstractCacheHelper;
import com.example.xiaomi.entity.Product;
import com.example.xiaomi.redis.IRedisService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ProductCacheHelper extends AbstractCacheHelper<Integer, Product> {
    @Autowired
    public ProductCacheHelper(
            @Qualifier("productCache") Cache<Integer, Product> localCache,
            IRedisService redisService
    ) {
        super(localCache, redisService);
    }
}