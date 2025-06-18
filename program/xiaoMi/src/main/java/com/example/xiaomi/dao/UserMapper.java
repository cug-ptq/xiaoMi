package com.example.xiaomi.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    String selectNameById(int id);
}
