package com.example.xiaomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@MapperScan("com.example.xiaomi.dao")
public class XiaoMiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoMiApplication.class, args);
    }

}
