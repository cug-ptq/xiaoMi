package com.example.xiaomi.service.impl;

import com.example.xiaomi.cache.impl.ProductCacheHelper;
import com.example.xiaomi.constant.Constants;
import com.example.xiaomi.dao.ProductMapper;
import com.example.xiaomi.entity.Product;
import com.example.xiaomi.redis.RedissonService;
import com.example.xiaomi.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductCacheHelper productCacheHelper;

    @Resource
    private RedissonService redissonService;
    @Resource
    private ProductMapper productMapper;

    @Override
    public int selectStockById(int id) {
        try {
            int stock = productMapper.selectStockById(id);
            log.info("id {} 的库存量 {}", id, stock);
            return stock;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product insertProduct(Product product) {
        try {
            productMapper.insertProduct(product);
            productCacheHelper.put(product.getId(),product,180_000);
            log.info("insert product success {} {}", product.getName(), product.getId());
            return product;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product selectProductById(int id) {
        Product product = null;
        try {
            if ((product = productCacheHelper.get(id)) != null) {
                log.info("查询缓存");
                return product;
            }

            product = productMapper.selectProductById(id);
            productCacheHelper.put(product.getId(),product,180_000);
            return product;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deduceStockById(Product product) {
        boolean success = false;
        boolean lock = false;
        RLock rLock = redissonService.getLock(Constants.PRODUCE_DEDUCE);;
        try {
            lock = rLock.tryLock(5,5, TimeUnit.SECONDS);
            if (lock) {
                log.info("获得锁");
                productMapper.deduceStockById(product);
                log.info("更新缓存");
                productCacheHelper.put(product.getId(),product, 180_000);
                success = true;
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        finally {
            // 释放锁（如果成功加锁了）
            if (lock) {
                rLock.unlock();
                System.out.println("Lock released");
            }
        }
        return success;
    }
}
