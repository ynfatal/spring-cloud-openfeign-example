package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 OpenFeign 的简单特性
 * @author Fatal
 * @date 2020/6/23
 */
@RestController
public class SimpleProviderController {

    @GetMapping("/hello")
    public String hello() {
        return "hello, Fatal!";
    }

}
