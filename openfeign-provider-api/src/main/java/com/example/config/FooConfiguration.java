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
        return new Retryer.Default();
    }

}
