package com.example.xiaomi.service;

import com.example.xiaomi.entity.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductService {
    /**
     * 查询库存  ID查询
     */

    int selectStockById(int id);

    /**
     * 商品添加
     */
    Product insertProduct(Product product);

    /**
     * 查询实体
     */
    Product selectProductById(int id);

    /**
     * 扣减
     * @return 实体
     */
    boolean deduceStockById(Product product);

}
