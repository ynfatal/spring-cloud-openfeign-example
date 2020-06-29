package com.example.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

/**
 * 局部 FeignClient 配置类，作用于指定的 FeignClient
 * 只作为配置类，不托管 Spring 容器，所以它可以不标注 @Configuration，但它下边的 Bean 必须标注 @Bean
 * @author Fatal
 * @date 2020/6/29 8:40
 */
public class FooConfiguration {

    @Bean
    public Retryer feignRetryer() {
        // 测试自定义全局 feign 配置 和 自定义局部 feign 配置的优先级
        return Retryer.NEVER_RETRY;
        // 测试默认 feign 配置 和 自定义局部 feign 配置的优先级
//        return new Retryer.Default();
    }

}
