package com.example.feign;

import com.example.feign.fallback.InheritFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Fatal
 * @date 2020/6/24 0023 5:39
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE", fallback = InheritFallback.class)
public interface IInheritClient {

    @GetMapping("/inherit")
    String inherit();

}
