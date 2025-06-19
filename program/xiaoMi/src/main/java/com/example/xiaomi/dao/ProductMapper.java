package com.example.xiaomi.dao;

import com.example.xiaomi.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
    /**
     * 查询库存  ID查询
     */

    int selectStockById(@Param("id") int id);

    /**
     * 商品添加
     */
    int insertProduct(Product product);

    /**
     * 查询实体
     */
    Product selectProductById(@Param("id") int id);

    /**
     * 扣减库存  ID 扣减量
     */
    int deduceStockById(Product product);
}
