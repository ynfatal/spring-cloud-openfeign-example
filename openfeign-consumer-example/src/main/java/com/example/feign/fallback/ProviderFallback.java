package com.example.feign.fallback;

import com.example.feign.IProviderClient;
import org.springframework.stereotype.Component;

/**
 * @author Fatal
 * @date 2020/6/23 0023 21:55
 */
@Component
public class ProviderFallback implements IProviderClient {

    @Override
    public String hello() {
        return "The hello method return fallback value";
    }
}
