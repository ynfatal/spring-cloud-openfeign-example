package com.example;

import com.example.config.FooConfiguration;
import java.util.stream.Stream;

import com.example.config.FeignConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 加 @Configuration 理解为配置组件，没加的话理解为配置类。
 * 配置组件托管 Spring 容器，配置类不托管。
 * 以该 example 为例子，FeignConfiguration是配置组件，FooConfiguration是配置类。
 */
@SpringBootApplication
@EnableEurekaClient
/*@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
//            FooConfiguration.class,      // 局部 FeignClient 配置类如果标注为配置组件，可以在这里排掉它
//            FeignConfiguration.class
        }
    )
})*/
// 开启 FeignClient 注册
@EnableFeignClients(defaultConfiguration = FeignConfiguration.class)
public class OpenfeignConsumerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenfeignConsumerExampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            System.out.println("看看当前 Spring 容器中的 Bean: ");
            Stream.of(context.getBeanDefinitionNames())
                .sorted()
                .forEach(System.out::println);
        };
    }
}
