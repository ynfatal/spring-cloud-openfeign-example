package com.example.feign.fallback;

import com.example.feign.ISimpleClient;
import org.springframework.stereotype.Component;

/**
 * 服务降级实现，必须添加 @Component 将其声明为 Spring 容器组件
 * @author Fatal
 * @date 2020/6/23 0023 21:55
 */
@Component
public class SimpleFallback implements ISimpleClient {

    @Override
    public String hello() {
        return "The hello method return fallback value";
    }
}
