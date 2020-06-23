package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Belo-betty
 * @date 2020/6/23
 */
@RestController
public class ProviderController {

    @GetMapping("/hello")
    public String hello() {
        return "hello, Fatal!";
    }

}
