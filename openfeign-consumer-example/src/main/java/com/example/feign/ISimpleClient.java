package com.example.feign;

import com.example.config.FooConfiguration;
import com.example.feign.fallback.SimpleFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @implNote
 * 1. FeignClient
 *   value: 服务器名
 *   fallback: 服务降级实现
 *   contextId: FeignClient Bean 的名称
 *   configuration: 声明额外的配置，指定的 Configuration 不需要加 @Configuration注解（参考官方文档）。
 * 2. FeignClient 的默认配置在 FeignClientsConfiguration
 * @author Fatal
 * @date 2020/6/23 0023 21:51
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE",
        fallback = SimpleFallback.class,
        contextId = "ISimpleClient",
        configuration = FooConfiguration.class)
public interface ISimpleClient {

    @GetMapping("/hello")
    String hello();

}
