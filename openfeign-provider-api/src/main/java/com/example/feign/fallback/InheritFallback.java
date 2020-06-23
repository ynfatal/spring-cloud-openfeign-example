package com.example.feign.fallback;

import com.example.feign.IInheritClient;
import org.springframework.stereotype.Component;

/**
 * @author Fatal
 * @date 2020/6/24 0023 5:39
 */
@Component
public class InheritFallback implements IInheritClient {

    @Override
    public String inherit() {
        return "The inherit() method return fallback value";
    }
}
