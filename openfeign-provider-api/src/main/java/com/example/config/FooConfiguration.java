package com.example.config;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 Feign 配置
 * @implNote
 * 1. FooConfiguration 不需要用 @Configuration 注释。
 * 2. Spring Cloud Netflix默认为feign提供以下bean（BeanType beanName: ClassName）:
 * - Decoder feignDecoder: ResponseEntityDecoder (包装了一个 SpringDecoder)
 * - Encoder feignEncoder: SpringEncoder
 * - Logger feignLogger: Slf4jLogger
 * - Contract feignContract: SpringMvcContract
 * - Feign.Builder feignBuilder: HystrixFeign.Builder
 * - Client feignClient: 如果 Ribbon 在类路径上被使用，那么它就是 LoadBalancerFeignClient，否则，如果 Spring Cloud LoadBalancer
 *  在类路径上，则使用 FeignBlockingLoadBalancerClient。如果它们都不在类路径上，就使用默认的 feign client。
 * @author Belo-betty
 * @date 2020/6/28 8:47
 */
@Configuration
@AllArgsConstructor
public class FooConfiguration {

    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
            new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

}
