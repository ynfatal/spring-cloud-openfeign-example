package com.example.controller;

import com.example.config.FeignConfiguration;
import com.example.config.FooConfiguration;
import com.example.feign.IAnotherClient;
import com.example.feign.IInheritClient;
import com.example.feign.ISimpleClient;
import com.example.request.CustomRequest;
import feign.Retryer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fatal
 * @date 2020/6/23 0023 21:48
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ConsumerController {

    private ISimpleClient simpleClient;
    private IAnotherClient anotherClient;
    private IInheritClient inheritClient;
    private FeignContext feignContext;

    @GetMapping("/hello")
    public String hello() {
        return simpleClient.hello();
    }

    @GetMapping("/another")
    public String another() {
        return anotherClient.another();
    }

    @GetMapping("/feign_context")
    public FeignContext feignContext() {
        log.info("contextNames = [{}]", feignContext.getContextNames());
        log.info("ISimpleClient's configuration bean for Retryer is [{}]", feignContext.getInstance("ISimpleClient", Retryer.class));
        log.info("ISimpleClient's configuration bean is [{}]", feignContext.getInstance("ISimpleClient", FeignClientsConfiguration.class));
        log.info("IAnotherClient's configuration bean for Retryer is [{}](Customized)", feignContext.getInstance("IAnotherClient", Retryer.class));
        log.info("IAnotherClient's configuration bean is [{}](Customized)", feignContext.getInstance("IAnotherClient", FooConfiguration.class));
        log.info("IInheritClient's configuration bean for Retryer is [{}]", feignContext.getInstance("IInheritClient", Retryer.class));
        // IInheritClient 没有自定义的局部 Feign 配置 FooConfiguration，所以它使用的是全局默认的 Feign 配置，故在 FeignContext 找不到属于他的 FooConfiguration，所以这里结果为 null
        log.info("IInheritClient's FooConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FooConfiguration.class));
        log.info("IInheritClient's FeignClientsConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FeignClientsConfiguration.class));
        log.info("IInheritClient's FeignConfiguration bean is [{}]", feignContext.getInstance("IInheritClient", FeignConfiguration.class));
        return feignContext;
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
