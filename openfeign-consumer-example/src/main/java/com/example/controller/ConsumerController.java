package com.example.controller;

import com.example.feign.IInheritClient;
import com.example.feign.ISimpleClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fatal
 * @date 2020/6/23 0023 21:48
 */
@RestController
@AllArgsConstructor
public class ConsumerController {

    private ISimpleClient simpleClient;
    private IInheritClient inheritClient;

    @GetMapping("/hello")
    public String hello() {
        return simpleClient.hello();
    }

    @GetMapping("/inherit")
    public String inherit() {
        return inheritClient.inherit();
    }
}
