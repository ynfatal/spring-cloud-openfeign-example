package com.example.feign;

import com.example.feign.fallback.ProviderFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Fatal
 * @date 2020/6/23 0023 21:51
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE", fallback = ProviderFallback.class)
public interface IProviderClient {

    @GetMapping("/hello")
    String hello();

}
