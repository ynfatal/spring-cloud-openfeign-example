package com.example.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 局部 FeignClient 配置类，作用于指定的 FeignClient
 * 只作为配置类，不托管 Spring 容器，所以它可以不标注 @Configuration，但它下边的 Bean 必须标注 @Bean
 * @author Fatal
 * @date 2020/6/29 8:40
 */
//@Configuration(proxyBeanMethods = false)
public class FooConfiguration {

    /**
     * Bean 的名称不要和自定义全局配置类中相同类型的 Bean 的名称一样，名称相同的 Bean，以先实例化的为准，后边的不会被实例化。
     * 所以这里需要特别注意的是，名称不同之后，在指定的子上下文中就会出现两个相同类型的 Bean。
     * 以本配置为例就是子上下文中 Retryer 类型有两个 Bean，分别为 locateRetryer 和 feignRetryer，这时候我们需要指定其中一个
     * Bean 被优先考虑自动装配，我们可以给该 Bean 标注 @Primary 即可。
     * @return
     */
    @Bean
    @Primary
    public Retryer locateRetryer() {
        // 测试自定义全局 feign 配置 和 自定义局部 feign 配置的优先级
        return Retryer.NEVER_RETRY;
        // 测试默认 feign 配置 和 自定义局部 feign 配置的优先级
//        return new Retryer.Default();
    }

}
