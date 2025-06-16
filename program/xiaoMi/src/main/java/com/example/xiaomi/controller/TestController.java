package com.example.xiaomi.controller;

import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    public static void init(){
        System.out.println("init");
    }

    public String test(){
        return "test";
    }
}
