package com.example.xiaomi.controller;

import com.example.xiaomi.redis.IRedisService;
import com.example.xiaomi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

@RequestMapping("/api/xiaomi")
@RestController
@Slf4j
public class TestController {

    @Resource
    public UserService userService;
    @Resource
    public RocketMQTemplate rocketMQTemplate;
    @Resource
    public IRedisService redissonService;

    @GetMapping("/verify")
    public ResponseEntity<String> verify(String token) {
        log.info("验证 token：{}", token);
        if ("success".equals(token)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/success")
    public String success(){
        redissonService.setValue("ptq","彭铁骑",9000);
        return "test success by xfg";
    }

    @GetMapping("/get")
    public String get(){
        return redissonService.getValue("ptq");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return userService.getName(1);
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendMsg(@RequestParam String msg) {
        rocketMQTemplate.convertAndSend("test-topic", msg);
        return "Message sent: " + msg;
    }
}