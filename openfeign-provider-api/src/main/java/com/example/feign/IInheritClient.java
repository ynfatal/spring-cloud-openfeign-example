package com.example.feign;

import com.example.config.FooConfiguration;
import com.example.feign.fallback.InheritFallback;
import com.example.request.CustomRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author Fatal
 * @date 2020/6/24 0023 5:39
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE",
        fallback = InheritFallback.class,
        contextId = "IInheritClient",
        configuration = FooConfiguration.class)
public interface IInheritClient {

    /**
     * GET 无参
     * @return
     */
    @GetMapping("/inherit")
    String inherit();

    /**
     * GET 多参
     * @return
     */
    @GetMapping("/multi_parameters/{integer}/{longValue}")
    String multiParametersGet(@RequestParam("string") String string, @PathVariable("integer") Integer integer,
                              @PathVariable("longValue") Long longValue, @RequestParam("doubleValue") Double doubleValue);

    /**
     * FeignClient 的 GetMapping 接收 Bean 时，需用 @SpringQueryMap 修饰对象才能支持 Bean 作为参数。
     * @return
     */
    @GetMapping("/custom_request")
    String customGetRequest(@SpringQueryMap CustomRequest customRequest);

    /**
     * POST Json
     * @return
     */
    @PostMapping("/custom_request")
    String customPostRequest(@RequestBody CustomRequest customRequest);

    /**
     * POST 多参
     * @return
     */
    @PostMapping("/multi_parameters/{integer}/{longValue}")
    String multiParametersPost(@PathVariable("integer") Integer integer, @RequestBody CustomRequest customRequest,
                               @PathVariable("longValue") Long longValue);

}
