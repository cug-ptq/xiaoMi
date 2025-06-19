package com.example.xiaomi.cache;

import com.example.xiaomi.redis.IRedisService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 二级缓存
 */
public abstract class AbstractCacheHelper<K, V> {
    protected final Cache<K, V> localCache;
    protected final IRedisService redisService;

    public AbstractCacheHelper(Cache<K, V> localCache, IRedisService redisService) {
        this.localCache = localCache;
        this.redisService = redisService;
    }

    public V get(K key) {
        V value = localCache.getIfPresent(key);
        if (value != null) return value;

        value = redisService.getValue(String.valueOf(key));
        if (value != null) localCache.put(key, value);
        return value;
    }

    public V put(K key, V value, long ttlSeconds) {
        localCache.put(key, value);
        redisService.setValue(String.valueOf(key), value, ttlSeconds);
        return value;
    }

    public void evict(K key) {
        localCache.invalidate(key);
        redisService.delete(String.valueOf(key));
    }
}