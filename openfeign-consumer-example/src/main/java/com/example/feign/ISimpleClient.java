package com.example.feign;

import com.example.feign.fallback.SimpleFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FeignClient
 *   value: 服务器名
 *   fallback: 服务降级实现
 * @author Fatal
 * @date 2020/6/23 0023 21:51
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE", fallback = SimpleFallback.class)
public interface ISimpleClient {

    @GetMapping("/hello")
    String hello();

}
