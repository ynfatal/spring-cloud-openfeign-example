package com.example.config;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.DefaultFeignLoggerFactory;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.util.List;

/**
 * 自定义 Feign 配置
 * @implNote
 * 1. FeignConfiguration 不需要用 @Configuration 注释。但是，如果选择使用，那么需要将它从任何可能包含此配置
 * 的 @ComponentScan 中排除，否则它被指定后将会变成 Feign feign.Decoder, feign.Encoder, feign.Contract 等等
 * 的默认配置源（全局）。可以放在 @ComponentScan 扫描不到的地方或者使用 @ComponentScan 来排除它。
 * 结论：全局的话可以加，局部的话不加。
 * 2. Spring Cloud Netflix 默认为 feign 提供以下 bean（BeanType beanName: ClassName）:
 * - Decoder feignDecoder: ResponseEntityDecoder (包装了一个 SpringDecoder)
 * - Encoder feignEncoder: SpringEncoder
 * - Logger feignLogger: Slf4jLogger
 * - Contract feignContract: SpringMvcContract
 * - Feign.Builder feignBuilder: HystrixFeign.Builder
 * - Client feignClient: 如果 Ribbon 在类路径上被使用，那么它就是 LoadBalancerFeignClient，否则，如果 Spring Cloud LoadBalancer
 *  在类路径上，则使用 FeignBlockingLoadBalancerClient。如果它们都不在类路径上，就使用默认的 feign client。
 * 3. FeignClientsConfiguration 也被标注了 @Configuration，它里边也配置了很多 Bean，为什么这个配置组件包括它包含的 Bean 没有被
 * Spring 容器管理，答案是 @ComponentScan 是扫描不到这个配置组件，所以它没有托管 Spring 容器。
 * 4. 本来以我对 @EnableFeignClient 的理解，我认为配置 @EnableFeignClient 的 defaultConfiguration 可以定制全局的 FeignClient 配置，
 * 不过按照目前的测试结果来看，使用 @EnableFeignClients(defaultConfiguration = FeignConfiguration.class) 配置的话优先级太高了，
 * 自定义局部配置都无法将其覆盖，于是优先级就变成了
 * `自定义全局 > 自定义局部 > 默认全局`
 * 配置行为如下：
 * - 自定义全局：使用 org.springframework.cloud.openfeign.EnableFeignClients#defaultConfiguration() 指定自定义全局配置类，该配置类不标注为配置组件
 * - 自定义局部：使用 org.springframework.cloud.openfeign.FeignClient#configuration() 指定自定义局部配置类，该配置类不标注为配置组件
 * - 默认全局：使用默认的方式，整合后什么都不搞
 * 当然，想要将优先级变为我们想要的
 * `自定义局部 > 自定义全部 > 默认全局`
 * 需要修改 自定义全局 配置的行为
 * - 自定义全局：不使用 org.springframework.cloud.openfeign.EnableFeignClients#defaultConfiguration() ，但要将该配置类标注为配置组件
 * 结论：@EnableFeignClients 的 defaultConfiguration 真的是令人匪夷所思，优先级竟然最高（除去配置文件外），要不就是它真的就是想提供这样的
 * 功能，不过个人觉得这功能不太友善，要不这就是一个 bug，要不就是我找不到正确的姿势。
 * @author Fatal
 * @date 2020/6/28 8:47
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
public class FeignConfiguration {

    private ObjectFactory<HttpMessageConverters> messageConverters;
    private List<AnnotatedParameterProcessor> parameterProcessors;
    private List<FeignFormatterRegistrar> feignFormatterRegistrars;

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
            new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    @Bean
    public Contract feignContract(ConversionService feignConversionService) {
        return new SpringMvcContract(this.parameterProcessors, feignConversionService);
    }

    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        for (FeignFormatterRegistrar feignFormatterRegistrar : this.feignFormatterRegistrars) {
            feignFormatterRegistrar.registerFormatters(conversionService);
        }
        return conversionService;
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(Retryer retryer) {
        return Feign.builder().retryer(retryer);
    }

    @Bean
    public Logger slf4jLogger() {
        return new Slf4jLogger();
    }

    @Bean
    public FeignLoggerFactory feignLoggerFactory(Logger slf4jLogger) {
        return new DefaultFeignLoggerFactory(slf4jLogger);
    }

}
