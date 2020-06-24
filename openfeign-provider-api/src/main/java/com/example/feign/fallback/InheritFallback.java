package com.example.feign.fallback;

import com.example.feign.IInheritClient;
import com.example.request.CustomGetRequest;
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

    @Override
    public String multiparameter(String string, Integer integer, Long longValue, Double doubleValue) {
        return "The multiparameter(..) method return fallback value";
    }

    @Override
    public String customRequest(CustomGetRequest customGetRequest) {
        return "The multiparameter(..) method return fallback value";
    }
}
