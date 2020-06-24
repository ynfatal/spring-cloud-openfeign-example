package com.example;

import java.util.stream.Stream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
// 开启 FeignClient 注册
@EnableFeignClients
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
