package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 FeignClient 的 FeignClientSpecification
 * @author Fatal
 * @date 2020/6/28 0028 5:53
 */
@RestController
public class AnotherProviderController {

    @GetMapping("/another")
    public String another() {
        return "another method called";
    }

}
