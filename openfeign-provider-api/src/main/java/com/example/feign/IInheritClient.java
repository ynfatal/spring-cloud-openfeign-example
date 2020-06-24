package com.example.feign;

import com.example.feign.fallback.InheritFallback;
import com.example.request.CustomGetRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Fatal
 * @date 2020/6/24 0023 5:39
 */
@FeignClient(value = "OPENFEIGN-PROVIDER-EXAMPLE", fallback = InheritFallback.class)
public interface IInheritClient {

    @GetMapping("/inherit")
    String inherit();

    @GetMapping("/multiparameter")
    String multiparameter(@RequestParam("string") String string, @RequestParam("integer") Integer integer,
                          @RequestParam("longValue") Long longValue, @RequestParam("doubleValue") Double doubleValue);

    /**
     * FeignClient 的 GetMapping 接收 Bean 时，需用 @SpringQueryMap 修饰对象才能支持 Bean 作为参数。
     * @param customGetRequest
     * @return
     */
    @GetMapping("/custom_request")
    String customRequest(@SpringQueryMap CustomGetRequest customGetRequest);
}
