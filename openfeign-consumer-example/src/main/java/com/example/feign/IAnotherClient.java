package com.example.feign;

import com.example.config.FooConfiguration;
import com.example.feign.fallback.AnotherFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Fatal
 * @date 2020/6/28 0028 5:56
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE",
        fallback = AnotherFallback.class,
        contextId = "IAnotherClient",
        configuration = FooConfiguration.class)
public interface IAnotherClient {

    @GetMapping("/another")
    String another();

}
