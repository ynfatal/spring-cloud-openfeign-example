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
 * 4. 同时使用自定义局部配置和自定义全局配置的话，配置行为如下：
 * - 自定义全局：使用 org.springframework.cloud.openfeign.EnableFeignClients#defaultConfiguration() 指定自定义全局配置类，该配置类不需要标注为配置组件
 * - 自定义局部：使用 org.springframework.cloud.openfeign.FeignClient#configuration() 指定自定义局部配置类，该配置类不需要标注为配置组件
 * 注意事项：
 * - 相同类型的 Bean，在自定义局部配置类和自定义全局配置类中不能重复，否则会造成局部的 Bean 无法实例化，这样局部就没作用了
 * - 在自义局部配置类给该 Bean 添加标注 @Primary，以表示该类型的 Bean 自定义局部配置高于自定义全局配置。
 * - 如果是默认全局与其中一个自定义配置搭配，那么随便玩，要是怕出问题也可以以当前这个最严谨的方式配置，也就是两点注意事项。
 * 扩展：
 * 其实这里的自定义配置没必要标注 @Configuration，因为这里的配置是给字上下文用的，标注后，在父上下文中又没用。
 * 如果在父上下文中需要用到，可以添加标注，否则，去掉吧。
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
