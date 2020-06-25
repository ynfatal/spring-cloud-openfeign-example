package com.example.controller;

import com.example.feign.IInheritClient;
import com.example.feign.ISimpleClient;
import com.example.request.CustomRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/multi_parameters")
    public String multiParametersGet() {
        return inheritClient.multiParametersGet("multiParametersGet", 18, 22L, 104.5D);
    }

    @GetMapping("/custom_request")
    public String customGetRequest() {
        return inheritClient.customGetRequest(new CustomRequest("customGetRequest", 22, 120L, 104.5D));
    }

    @PostMapping("/custom_request")
    public String customPostRequest() {
        return inheritClient.customPostRequest(new CustomRequest("customPostRequest", 22, 120L, 104.5D));
    }

    @PostMapping("/multi_parameters")
    public String multiParametersPost() {
        return inheritClient.multiParametersPost(16, new CustomRequest("multiParametersPost", 18, 22L, 104.5D), 33L);
    }

}
