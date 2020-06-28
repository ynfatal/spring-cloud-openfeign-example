package com.example.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 自定义 Feign 配置
 * @implNote
 * 1. FooConfiguration 不需要用 @Configuration 注释。
 *
 * @author Belo-betty
 * @date 2020/6/28 8:47
 */
public class FooConfiguration {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }

}
