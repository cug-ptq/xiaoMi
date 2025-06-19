package com.example.xiaomi.controller;

import com.example.xiaomi.constant.Constants;
import com.example.xiaomi.entity.Employee;
import com.example.xiaomi.entity.Product;
import com.example.xiaomi.entity.Response;
import com.example.xiaomi.service.EmployeeService;
import com.example.xiaomi.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/api/xiaomi/product")
@Slf4j
@RestController
public class ProductController {
    @Resource
    public ProductService productService;

    /**
     * 新增
     * @param product json传入
     * @return 新增是否成功，携带生成的ID
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Response<String> addEmployee(@RequestBody Product product) {
        try {
            Product result = productService.insertProduct(product);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.ADD_ERROR.getCode())
                    .info(Constants.ResponseCode.ADD_ERROR.getInfo())
                    .build();
        }
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .build();

    }

    /**
     * 扣减库存
     * @param product 必须包含 ID 和 扣减的 stock 正数
     * @return 更新是否成功
     */
    @RequestMapping(value = "/deduce",method = RequestMethod.PUT)
    public Response<String> deduceStockById(@RequestBody Product product) {
        boolean success = productService.deduceStockById(product);
        if (success){
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } else {
            return Response.<String>builder()
                    .code(Constants.ResponseCode.DELETE_ERROR.getCode())
                    .info(Constants.ResponseCode.DELETE_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return 实体信息
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Response<String> getEmployeeById(@PathVariable int id) {
        Product product;
        try {
            product = productService.selectProductById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SELECT_ERROR.getCode())
                    .info(Constants.ResponseCode.SELECT_ERROR.getInfo())
                    .build();

        }
        return Response.<String>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getInfo())
                .data(product.toString())
                .build();
    }
}
