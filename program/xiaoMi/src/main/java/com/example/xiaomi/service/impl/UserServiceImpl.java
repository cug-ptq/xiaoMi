package com.example.xiaomi.service.impl;

import com.example.xiaomi.dao.UserMapper;
import com.example.xiaomi.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Override
    public String getName(int id) {
        return userMapper.selectNameById(id);
    }
}
