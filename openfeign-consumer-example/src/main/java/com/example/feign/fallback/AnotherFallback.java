package com.example.feign.fallback;

import com.example.feign.IAnotherClient;
import org.springframework.stereotype.Component;

/**
 * @author Fatal
 * @date 2020/6/28 0028 5:57
 */
@Component
public class AnotherFallback implements IAnotherClient {

    @Override
    public String another() {
        return "The another method return fallback value";
    }

}
